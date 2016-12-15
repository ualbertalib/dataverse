package edu.harvard.iq.dataverse.passwordreset;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import edu.harvard.iq.dataverse.MailServiceBean;
import edu.harvard.iq.dataverse.authorization.providers.builtin.BuiltinUser;
import edu.harvard.iq.dataverse.authorization.providers.builtin.BuiltinUserServiceBean;
import edu.harvard.iq.dataverse.authorization.providers.builtin.PasswordEncryption;
import edu.harvard.iq.dataverse.util.SystemConfig;

@Stateless
@Named
public class PasswordResetServiceBean {

    private static final Logger logger = Logger.getLogger(PasswordResetServiceBean.class.getCanonicalName());

    @EJB
    BuiltinUserServiceBean dataverseUserService;

    @EJB
    MailServiceBean mailService;

    @PersistenceContext(unitName = "VDCNet-ejbPU")
    private EntityManager em;

    /**
     * Initiate the password reset process.
     *
     * @param emailAddress
     * @return {@link PasswordResetInitResponse}
     * @throws edu.harvard.iq.dataverse.passwordreset.PasswordResetException
     */
    // inspired by Troy Hunt: Everything you ever wanted to know about building a secure password reset feature - http://www.troyhunt.com/2012/05/everything-you-ever-wanted-to-know.html
    public PasswordResetInitResponse requestReset(final String emailAddress) throws PasswordResetException {
        deleteAllExpiredTokens();
        final BuiltinUser user = dataverseUserService.findByEmail(emailAddress);
        if (user != null) {
            return requestPasswordReset( user, true, PasswordResetData.Reason.FORGOT_PASSWORD );
        } else {
            return new PasswordResetInitResponse(false);
        }
    }
    
    public PasswordResetInitResponse requestPasswordReset( final BuiltinUser aUser, final boolean sendEmail, final PasswordResetData.Reason reason ) throws PasswordResetException {
        // delete old tokens for the user
        final List<PasswordResetData> oldTokens = findPasswordResetDataByDataverseUser(aUser);
        for (final PasswordResetData oldToken : oldTokens) {
            em.remove(oldToken);
        }
        
        // create a fresh token for the user
        final PasswordResetData passwordResetData = new PasswordResetData(aUser);
        passwordResetData.setReason(reason);
        try {
            em.persist(passwordResetData);
            final PasswordResetInitResponse passwordResetInitResponse = new PasswordResetInitResponse(true, passwordResetData);
            if ( sendEmail ) {
                sendPasswordResetEmail(aUser, passwordResetInitResponse.getResetUrl());
            }

            return passwordResetInitResponse;
            
        } catch (final Exception ex) {
            final String msg = "Unable to save token for " + aUser.getEmail();
            throw new PasswordResetException(msg, ex);
        }
        
    }

    private void sendPasswordResetEmail(final BuiltinUser aUser, final String passwordResetUrl) throws PasswordResetException {
        final String messageBody = MessageFormat
            .format(ResourceBundle.getBundle("Bundle").getString("notification.email.resetPassword"),
                aUser.getDisplayName(), aUser.getUserName(), passwordResetUrl,
                SystemConfig.getMinutesUntilPasswordResetTokenExpires());
        try {
            final String toAddress = aUser.getEmail();
            final String subject =
                ResourceBundle.getBundle("Bundle").getString("notification.email.reset.password.subject");
            mailService.sendSystemEmail(toAddress, subject, messageBody);
        } catch (final Exception ex) {
            /**
             * @todo get more specific about the exception that's thrown
             * when `asadmin create-javamail-resource` (or equivalent)
             * hasn't been run.
             */
            throw new PasswordResetException("Problem sending password reset email possibily due to mail server not being configured.");
        }
        logger.log(Level.INFO, "attempted to send mail to {0}", aUser.getEmail());
    }
    
    /**
     * Process the password reset token, allowing the user to reset the password
     * or report on a invalid token.
     *
     * @param tokenQueried
     */
    public PasswordResetExecResponse processToken(final String tokenQueried) {
        deleteAllExpiredTokens();
        final PasswordResetExecResponse tokenUnusable = new PasswordResetExecResponse(tokenQueried, null);
        final PasswordResetData passwordResetData = findSinglePasswordResetDataByToken(tokenQueried);
        if (passwordResetData != null) {
            if (passwordResetData.isExpired()) {
                // shouldn't reach here since tokens are being expired above
                return tokenUnusable;
            } else {
                final PasswordResetExecResponse goodTokenCanProceed = new PasswordResetExecResponse(tokenQueried, passwordResetData);
                return goodTokenCanProceed;
            }
        } else {
            return tokenUnusable;
        }
    }

    /**
     * @param token
     * @return Null or a single row of password reset data.
     */
    private PasswordResetData findSinglePasswordResetDataByToken(final String token) {
        PasswordResetData passwordResetData = null;
        final TypedQuery<PasswordResetData> typedQuery = em.createNamedQuery("PasswordResetData.findByToken", PasswordResetData.class);
        typedQuery.setParameter("token", token);
        try {
            passwordResetData = typedQuery.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            logger.info("When looking up " + token + " caught " + ex);
        }
        return passwordResetData;
    }

    public List<PasswordResetData> findPasswordResetDataByDataverseUser(final BuiltinUser user) {
        final TypedQuery<PasswordResetData> typedQuery = em.createNamedQuery("PasswordResetData.findByUser", PasswordResetData.class);
        typedQuery.setParameter("user", user);
        final List<PasswordResetData> passwordResetDatas = typedQuery.getResultList();
        return passwordResetDatas;
    }

    public List<PasswordResetData> findAllPasswordResetData() {
        final TypedQuery<PasswordResetData> typedQuery = em.createNamedQuery("PasswordResetData.findAll", PasswordResetData.class);
        final List<PasswordResetData> passwordResetDatas = typedQuery.getResultList();
        return passwordResetDatas;
    }

    /**
     * @return The number of tokens deleted.
     */
    private long deleteAllExpiredTokens() {
        long numDeleted = 0;
        final List<PasswordResetData> allData = findAllPasswordResetData();
        for (final PasswordResetData data : allData) {
            if (data.isExpired()) {
                em.remove(data);
                numDeleted++;
            }
        }
        return numDeleted;
    }

    public PasswordChangeAttemptResponse attemptPasswordReset(final BuiltinUser user, final String newPassword, final String token) {

        final String messageSummarySuccess =
            ResourceBundle.getBundle("Bundle").getString("passwdReset.validation.successSummary");
        final String messageDetailSuccess = "";

        // optimistic defaults :)
        String messageSummary = messageSummarySuccess;
        String messageDetail = messageDetailSuccess;

        final String messageSummaryFail =
            ResourceBundle.getBundle("Bundle").getString("passwdReset.validation.failSummary");
        if (user == null) {
            messageSummary = messageSummaryFail;
            messageDetail = ResourceBundle.getBundle("Bundle").getString("passwdReset.validation.userNotFound");
            return new PasswordChangeAttemptResponse(false, messageSummary, messageDetail);
        }
        if (newPassword == null) {
            messageSummary = messageSummaryFail;
            messageDetail = ResourceBundle.getBundle("Bundle").getString("passwdReset.validation.passwordNotProvided");
            return new PasswordChangeAttemptResponse(false, messageSummary, messageDetail);
        }
        if (token == null) {
            logger.info("No token provided... won't be able to delete it. Let the user change the password though.");
        }

        /**
         * @todo move these rules deeper into the system
         */
        final int minPasswordLength = 6;
        final boolean forceNumber = true;
        final boolean forceSpecialChar = false;
        final boolean forceCapitalLetter = false;
        final int maxPasswordLength = 255;
        /**
         *
         * @todo move the business rules for password complexity (once we've
         * defined them in https://github.com/IQSS/dataverse/issues/694 ) deeper
         * into the system and have all calls to
         * DataverseUser.setEncryptedPassword call into the password complexity
         * validataion method.
         *
         * @todo maybe look into why with the combination of minimum 8
         * characters, max 255 characters, all other rules disabled that the
         * password "12345678" is not considered valid.
         */
        final PasswordValidator validator = PasswordValidator.buildValidator(forceSpecialChar, forceCapitalLetter, forceNumber, minPasswordLength, maxPasswordLength);
        final boolean passwordIsComplexEnough = validator.validatePassword(newPassword);
        if (!passwordIsComplexEnough) {
            messageSummary = messageSummaryFail;
            messageDetail = MessageFormat
                .format(ResourceBundle.getBundle("Bundle").getString("passwdReset.validation.failDetail"),
                    minPasswordLength);
            logger.info(messageDetail);
            return new PasswordChangeAttemptResponse(false, messageSummary, messageDetail);
        }
        
        final String newHashedPass = PasswordEncryption.get().encrypt(newPassword);
        final int latestVersionNumber = PasswordEncryption.getLatestVersionNumber();
        user.updateEncryptedPassword(newHashedPass, latestVersionNumber);
        final BuiltinUser savedUser = dataverseUserService.save(user);
        
        if (savedUser != null) {
            messageSummary = messageSummarySuccess;
            messageDetail = messageDetailSuccess;
            final boolean tokenDeleted = deleteToken(token);
            if (!tokenDeleted) {
                // suboptimal but when it expires it should be deleted
                logger.info("token " + token + " for user id " + user.getId() + " was not deleted");
            }
            final String toAddress = user.getEmail();
            final String subject =
                ResourceBundle.getBundle("Bundle").getString("notification.email.reset.passwordSuccess.subject");

            final String messageBody = MessageFormat.format(
                ResourceBundle.getBundle("Bundle").getString("notification.email.resetPasswordSuccess"),
                user.getDisplayName());
            mailService.sendSystemEmail(toAddress, subject, messageBody);
            return new PasswordChangeAttemptResponse(true, messageSummary, messageDetail);
        } else {
            messageSummary = messageSummaryFail;
            messageDetail = ResourceBundle.getBundle("Bundle").getString("passwdReset.validation.notReset");
            logger.info("Enable to save user " + user.getId());
            return new PasswordChangeAttemptResponse(false, messageSummary, messageDetail);
        }

    }

    private boolean deleteToken(final String token) {
        final PasswordResetData doomed = findSinglePasswordResetDataByToken(token);
        try {
            em.remove(doomed);
            return true;
        } catch (final Exception ex) {
            logger.info("Caught exception trying to delete token " + token + " - " + ex);
            return false;
        }
    }
}
