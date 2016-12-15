/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.authorization.providers.builtin;

import static edu.harvard.iq.dataverse.util.JsfHelper.JH;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.event.TabChangeEvent;

import edu.harvard.iq.dataverse.DataFile;
import edu.harvard.iq.dataverse.DataFileServiceBean;
import edu.harvard.iq.dataverse.Dataset;
import edu.harvard.iq.dataverse.DatasetServiceBean;
import edu.harvard.iq.dataverse.DatasetVersionServiceBean;
import edu.harvard.iq.dataverse.Dataverse;
import edu.harvard.iq.dataverse.DataverseServiceBean;
import edu.harvard.iq.dataverse.DataverseSession;
import edu.harvard.iq.dataverse.DvObject;
import edu.harvard.iq.dataverse.PermissionServiceBean;
import edu.harvard.iq.dataverse.PermissionsWrapper;
import edu.harvard.iq.dataverse.RoleAssignment;
import edu.harvard.iq.dataverse.SettingsWrapper;
import edu.harvard.iq.dataverse.UserNotification;
import edu.harvard.iq.dataverse.UserNotificationServiceBean;
import edu.harvard.iq.dataverse.authorization.AuthenticationServiceBean;
import edu.harvard.iq.dataverse.authorization.UserRecordIdentifier;
import edu.harvard.iq.dataverse.authorization.groups.Group;
import edu.harvard.iq.dataverse.authorization.groups.GroupServiceBean;
import edu.harvard.iq.dataverse.authorization.users.AuthenticatedUser;
import edu.harvard.iq.dataverse.confirmemail.ConfirmEmailException;
import edu.harvard.iq.dataverse.confirmemail.ConfirmEmailInitResponse;
import edu.harvard.iq.dataverse.confirmemail.ConfirmEmailServiceBean;
import edu.harvard.iq.dataverse.confirmemail.ConfirmEmailUtil;
import edu.harvard.iq.dataverse.mydata.MyDataPage;
import edu.harvard.iq.dataverse.passwordreset.PasswordValidator;
import edu.harvard.iq.dataverse.settings.SettingsServiceBean;
import edu.harvard.iq.dataverse.util.BundleUtil;
import edu.harvard.iq.dataverse.util.JsfHelper;
import edu.harvard.iq.dataverse.util.SystemConfig;

/**
 *
 * @author xyang
 */
@ViewScoped
@Named("DataverseUserPage")
public class BuiltinUserPage implements java.io.Serializable {

    private static final Logger logger = Logger.getLogger(BuiltinUserPage.class.getCanonicalName());

    public enum EditMode {

        CREATE, EDIT, CHANGE_PASSWORD, FORGOT
    };

    @Inject
    DataverseSession session;
    @EJB
    DataverseServiceBean dataverseService;
    @EJB
    UserNotificationServiceBean userNotificationService;
    @EJB
    DatasetServiceBean datasetService;
    @EJB
    DataFileServiceBean fileService;
    @EJB
    DatasetVersionServiceBean datasetVersionService;
    @EJB
    PermissionServiceBean permissionService;
    @EJB
    BuiltinUserServiceBean builtinUserService;
    @EJB
    AuthenticationServiceBean authenticationService;
    @EJB
    ConfirmEmailServiceBean confirmEmailService;
    @EJB
    SystemConfig systemConfig;    
    @EJB
    GroupServiceBean groupService;
    @Inject
    SettingsWrapper settingsWrapper;
    @Inject
    MyDataPage mydatapage;
    @Inject
    PermissionsWrapper permissionsWrapper;
    
    @EJB
    AuthenticationServiceBean authSvc;

    private AuthenticatedUser currentUser;
    private BuiltinUser builtinUser;    
    private EditMode editMode;
    private String redirectPage = "dataverse.xhtml";    

    @NotBlank(message = "Please enter a password for your account.")
    private String inputPassword;

    @NotBlank(message = "Please enter a password for your account.")
    private String currentPassword;
    private Long dataverseId;
    private List<UserNotification> notificationsList;
    private int activeIndex;
    private String selectTab = "somedata";
    UIInput usernameField;
    
    public EditMode getChangePasswordMode () {
        return EditMode.CHANGE_PASSWORD;
    }

    public AuthenticatedUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public BuiltinUser getBuiltinUser() {
        return builtinUser;
    }

    public void setBuiltinUser(final BuiltinUser builtinUser) {
        this.builtinUser = builtinUser;
    }
     
    public EditMode getEditMode() {
        return editMode;
    }

    public void setEditMode(final EditMode editMode) {
        this.editMode = editMode;
    }

    public String getRedirectPage() {
        return redirectPage;
    }

    public void setRedirectPage(final String redirectPage) {
        this.redirectPage = redirectPage;
    } 

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(final String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(final String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public Long getDataverseId() {

        if (dataverseId == null) {
            dataverseId = dataverseService.findRootDataverse().getId();
        }
        return dataverseId;
    }

    public void setDataverseId(final Long dataverseId) {
        this.dataverseId = dataverseId;
    }



    public List getNotificationsList() {
        return notificationsList;
    }

    public void setNotificationsList(final List notificationsList) {
        this.notificationsList = notificationsList;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(final int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public String getSelectTab() {
        return selectTab;
    }

    public void setSelectTab(final String selectTab) {
        this.selectTab = selectTab;
    }

    public UIInput getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(final UIInput usernameField) {
        this.usernameField = usernameField;
    }

    public String init() {

        // prevent creating a user if signup not allowed.
        final boolean safeDefaultIfKeyNotFound = true;
        final boolean signupAllowed = settingsWrapper.isTrueForKey(SettingsServiceBean.Key.AllowSignUp.toString(), safeDefaultIfKeyNotFound);
        logger.fine("signup is allowed: " + signupAllowed);

        if (editMode == EditMode.CREATE && !signupAllowed) {
            return "/403.xhtml";
        }

        if (editMode == EditMode.CREATE) {
            if (!session.getUser().isAuthenticated()) { // in create mode for new user
                JH.addMessage(FacesMessage.SEVERITY_INFO, BundleUtil.getStringFromBundle("user.signup.tip"));
                builtinUser = new BuiltinUser();
                return "";
            } else {
                editMode = null; // we can't be in create mode for an existing user
            }
        }
        
        if ( session.getUser().isAuthenticated() ) {
            currentUser = (AuthenticatedUser) session.getUser();
            notificationsList = userNotificationService.findByUser(currentUser.getId());
            if (currentUser.isBuiltInUser()) {
                builtinUser =  builtinUserService.findByUserName(currentUser.getUserIdentifier());
            }
            switch (selectTab) {
                case "notifications":
                    activeIndex = 1;
                    displayNotification();
                    break;
                case "dataRelatedToMe":
                    mydatapage.init();
                    break;
                // case "groupsRoles":
                    // activeIndex = 2;
                    // break;
                case "accountInfo":
                    activeIndex = 2;
                    // activeIndex = 3;
                    break;
                case "apiTokenTab":
                    activeIndex = 3;
                    break;
                default:
                    activeIndex = 0;
                    break;
            }            
            
        } else {
            return permissionsWrapper.notAuthorized();
        }
        
        return "";
    }

    public void edit(final ActionEvent e) {
        editMode = EditMode.EDIT;
    }

    public void changePassword(final ActionEvent e) {
        editMode = EditMode.CHANGE_PASSWORD;
    }

    public void forgotPassword(final ActionEvent e) {
        editMode = EditMode.FORGOT;
    }

    public void validateUserName(final FacesContext context, final UIComponent toValidate, final Object value) {
        final String userName = (String) value;
        boolean userNameFound = false;
        final BuiltinUser user = builtinUserService.findByUserName(userName);
        if (editMode == EditMode.CREATE) {
            if (user != null) {
                userNameFound = true;
            }
        } else {
            if (user != null && !user.getId().equals(builtinUser.getId())) {
                userNameFound = true;
            }
        }
        if (userNameFound) {
            ((UIInput) toValidate).setValid(false);
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, JH.localize("user.username.taken"), null);
            context.addMessage(toValidate.getClientId(context), message);
        }
    }
    
    public void validateUserEmail(final FacesContext context, final UIComponent toValidate, final Object value) {
        final String userEmail = (String) value;
        boolean userEmailFound = false;
        final BuiltinUser user = builtinUserService.findByEmail(userEmail);
        final AuthenticatedUser aUser = authenticationService.getAuthenticatedUserByEmail(userEmail);
        if (editMode == EditMode.CREATE) {
            if (user != null || aUser != null) {
                userEmailFound = true;
            }
        } else {
            //In edit mode...
            if (user != null || aUser != null){
                 userEmailFound = true;               
            }
            //if there's a match on edit make sure that the email belongs to the 
            // user doing the editing by checking ids
            if ((user != null && user.getId().equals(builtinUser.getId())) || (aUser!=null && aUser.getId().equals(builtinUser.getId()))){
                userEmailFound = false;
            }
        }
        if (userEmailFound) {
            ((UIInput) toValidate).setValid(false);
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, JH.localize("user.email.taken"), null);
            context.addMessage(toValidate.getClientId(context), message);
        }
    }
    

    public void validateUserNameEmail(final FacesContext context, final UIComponent toValidate, final Object value) {
        final String userName = (String) value;
        boolean userNameFound = false;
        final BuiltinUser user = builtinUserService.findByUserName(userName);
        if (user != null) {
            userNameFound = true;
        } else {
            final BuiltinUser user2 = builtinUserService.findByEmail(userName);
            if (user2 != null) {
                userNameFound = true;
            }
        }
        if (!userNameFound) {
            ((UIInput) toValidate).setValid(false);
            final FacesMessage message = new FacesMessage("Username or Email is incorrect.");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public void validateCurrentPassword(final FacesContext context, final UIComponent toValidate, final Object value) {
        
        final String password = (String) value;
        
        if (StringUtils.isBlank(password)){
            logger.log(Level.WARNING, "current password is blank");
            
            ((UIInput) toValidate).setValid(false);
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Password Error", "Password is blank: re-type it again.");
            context.addMessage(toValidate.getClientId(context), message);
            return;
            
        } else {
            logger.log(Level.INFO, "current paswword is not blank");
        }
        
        
        
        if ( ! PasswordEncryption.getVersion(builtinUser.getPasswordEncryptionVersion()).check(password, builtinUser.getEncryptedPassword()) ) {
            ((UIInput) toValidate).setValid(false);
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Error", "Password is incorrect.");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }
    
    public void validateNewPassword(final FacesContext context, final UIComponent toValidate, final Object value) {
        final String password = (String) value;
        if (StringUtils.isBlank(password)){
            logger.log(Level.WARNING, "new password is blank");
            
            ((UIInput) toValidate).setValid(false);

            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Password Error", "The new password is blank: re-type it again");
            context.addMessage(toValidate.getClientId(context), message);
            return;
            
        } else {
            logger.log(Level.INFO, "new paswword is not blank");
        }

        final int minPasswordLength = 6;
        final boolean forceNumber = true;
        final boolean forceSpecialChar = false;
        final boolean forceCapitalLetter = false;
        final int maxPasswordLength = 255;

        final PasswordValidator validator = PasswordValidator.buildValidator(forceSpecialChar, forceCapitalLetter, forceNumber, minPasswordLength, maxPasswordLength);
        final boolean passwordIsComplexEnough = password!= null && validator.validatePassword(password);
        if (!passwordIsComplexEnough) {
            ((UIInput) toValidate).setValid(false);
            final String messageDetail = "Password is not complex enough. The password must have at least one letter, one number and be at least " + minPasswordLength + " characters in length.";
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Error", messageDetail);
            context.addMessage(toValidate.getClientId(context), message);
        }
    }
    
    

    public void updatePassword(final String userName) {
        final String plainTextPassword = PasswordEncryption.generateRandomPassword();
        BuiltinUser user = builtinUserService.findByUserName(userName);
        if (user == null) {
            user = builtinUserService.findByEmail(userName);
        }
        user.updateEncryptedPassword(PasswordEncryption.get().encrypt(plainTextPassword), PasswordEncryption.getLatestVersionNumber());
        builtinUserService.save(user);
    }

    public String save() {
        boolean passwordChanged = false;
        boolean emailChanged = false;
        if (editMode == EditMode.CREATE || editMode == EditMode.CHANGE_PASSWORD) {
            if (inputPassword != null) {
                builtinUser.updateEncryptedPassword(PasswordEncryption.get().encrypt(inputPassword), PasswordEncryption.getLatestVersionNumber());
                passwordChanged = true;
            } else {
                // just defensive coding: for in case when the validator is not
                // working
                logger.log(Level.WARNING, "inputPassword is still null");
                final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, JH.localize("user.noPasswd"), null);
                final FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, message);
                return null;
            }
        }
        builtinUser = builtinUserService.save(builtinUser);

        if (editMode == EditMode.CREATE) {
            final AuthenticatedUser au = authSvc.createAuthenticatedUser(
                    new UserRecordIdentifier(BuiltinAuthenticationProvider.PROVIDER_ID, builtinUser.getUserName()),
                    builtinUser.getUserName(), builtinUser.getDisplayInfo(), false);
            if ( au == null ) {
                // username exists
                getUsernameField().setValid(false);
                final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, JH.localize("user.username.taken"), null);
                final FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(getUsernameField().getClientId(context), message);
                return null;
            }

            // success message with email
            JsfHelper.addSuccessMessage(
                MessageFormat.format(BundleUtil.getStringFromBundle("user.create.success"), au.getEmail(),
                ConfirmEmailUtil.friendlyExpirationTime(systemConfig.getMinutesUntilConfirmEmailTokenExpires())));

            // remove automatically login
            // session.setUser(au);
            userNotificationService.sendNotification(au,
                                                     new Timestamp(new Date().getTime()), 
                                                     UserNotification.Type.CREATEACC, null);

            // go back to where user came from
            if ("dataverse.xhtml".equals(redirectPage)) {
                redirectPage = redirectPage + "&alias=" + dataverseService.findRootDataverse().getAlias();
            }
            
            try {            
                redirectPage = URLDecoder.decode(redirectPage, "UTF-8");
            } catch (final UnsupportedEncodingException ex) {
                Logger.getLogger(BuiltinUserPage.class.getName()).log(Level.SEVERE, null, ex);
                redirectPage = "dataverse.xhtml&alias=" + dataverseService.findRootDataverse().getAlias();
            }

            logger.log(Level.FINE, "Sending user to = {0}", redirectPage);

            return redirectPage + (!redirectPage.contains("?") ? "?" : "&") + "faces-redirect=true";            
            
            
        } else {
            final String emailBeforeUpdate = currentUser.getEmail();
            final AuthenticatedUser savedUser = authSvc.updateAuthenticatedUser(currentUser, builtinUser.getDisplayInfo());
            final String emailAfterUpdate = savedUser.getEmail();
            if (!emailBeforeUpdate.equals(emailAfterUpdate)) {
                emailChanged = true;
            }
            editMode = null;
            String msg = "Your account information has been successfully updated.";
            if (passwordChanged) {
                msg = "Your account password has been successfully changed.";
            }
            if (emailChanged) {
                final String expTime =
                    ConfirmEmailUtil.friendlyExpirationTime(systemConfig.getMinutesUntilConfirmEmailTokenExpires());
                msg = msg + " Your email address has changed and must be re-verified. Please check your inbox at " + currentUser.getEmail() + " and follow the link we've sent. \n\nAlso, please note that the link will only work for the next " + expTime + " before it has expired.";
                final boolean sendEmail = true;
                // delete unexpired token, if it exists (clean slate)
                confirmEmailService.deleteTokenForUser(currentUser);
                try {
                    final ConfirmEmailInitResponse confirmEmailInitResponse = confirmEmailService.beginConfirm(currentUser);
                } catch (final ConfirmEmailException ex) {
                    logger.info("Unable to send email confirmation link to user id " + savedUser.getId());
                }
                session.setUser(currentUser);
                JsfHelper.addSuccessMessage(msg);
            } else {
                JsfHelper.addFlashMessage(msg);
            }
            return null;            
        }
    }

    public String cancel() {
        if (editMode == EditMode.CREATE) {
            return "/dataverse.xhtml?alias=" + dataverseService.findRootDataverse().getAlias() + "&faces-redirect=true";
        }

        editMode = null;
        return null;
    }

    public void submit(final ActionEvent e) {
        updatePassword(builtinUser.getUserName());
        editMode = null;
    }

    public String remove(final Long notificationId) {
        final UserNotification userNotification = userNotificationService.find(notificationId);
        userNotificationService.delete(userNotification);
        for (final UserNotification uNotification : notificationsList) {
            if (uNotification.getId() == userNotification.getId()) {
                notificationsList.remove(uNotification);
                break;
            }
        }
        return null;
    }

    public void onTabChange(final TabChangeEvent event) {
        if (event.getTab().getId().equals("notifications")) {
            displayNotification();
        }
        if (event.getTab().getId().equals("dataRelatedToMe")){
            mydatapage.init();
        }
    }
    
    private String getRoleStringFromUser(final AuthenticatedUser au, final DvObject dvObj) {
        // Find user's role(s) for given dataverse/dataset
        final Set<RoleAssignment> roles = permissionService.assignmentsFor(au, dvObj);
        final List<String> roleNames = new ArrayList();

        // Include roles derived from a user's groups
        final Set<Group> groupsUserBelongsTo = groupService.groupsFor(au, dvObj);
        for (final Group g : groupsUserBelongsTo) {
            roles.addAll(permissionService.assignmentsFor(g, dvObj));
        }

        for (final RoleAssignment ra : roles) {
            roleNames.add(ra.getRole().getName());
        }
        if (roleNames.isEmpty()){
            return "[Unknown]";
        }
        return StringUtils.join(roleNames, "/");
    }

    public void displayNotification() {
        for (final UserNotification userNotification : notificationsList) {
            switch (userNotification.getType()) {
                case ASSIGNROLE:   
                case REVOKEROLE:
                    // Can either be a dataverse or dataset, so search both
                    final Dataverse dataverse = dataverseService.find(userNotification.getObjectId());
                    if (dataverse != null) {
                        userNotification.setRoleString(this.getRoleStringFromUser(this.getCurrentUser(), dataverse ));
                        userNotification.setTheObject(dataverse);
                    } else {
                        final Dataset dataset = datasetService.find(userNotification.getObjectId());
                        if (dataset != null){
                            userNotification.setRoleString(this.getRoleStringFromUser(this.getCurrentUser(), dataset ));
                            userNotification.setTheObject(dataset);
                        } else {
                            final DataFile datafile = fileService.find(userNotification.getObjectId());
                            userNotification.setRoleString(this.getRoleStringFromUser(this.getCurrentUser(), datafile ));
                            userNotification.setTheObject(datafile);
                        }
                    }
                    break;
                case CREATEDV:
                    userNotification.setTheObject(dataverseService.find(userNotification.getObjectId()));
                    break;
 
                case REQUESTFILEACCESS:
                    final DataFile file = fileService.find(userNotification.getObjectId());
                    userNotification.setTheObject(file.getOwner());
                    break;
                case GRANTFILEACCESS:
                case REJECTFILEACCESS:
                    userNotification.setTheObject(datasetService.find(userNotification.getObjectId()));
                    break;
                    
                case MAPLAYERUPDATED:
                case CREATEDS:
                case SUBMITTEDDS:
                case PUBLISHEDDS:
                case RETURNEDDS:
                    userNotification.setTheObject(datasetVersionService.find(userNotification.getObjectId()));
                    break;

                case CREATEACC:
                    userNotification.setTheObject(userNotification.getUser());
            }

            userNotification.setDisplayAsRead(userNotification.isReadNotification());
            if (userNotification.isReadNotification() == false) {
                userNotification.setReadNotification(true);
                userNotificationService.save(userNotification);
            }
        }
    }
    
    public void sendConfirmEmail() {
        logger.fine("called sendConfirmEmail()");
        final String userEmail = currentUser.getEmail();
        
        try {
            confirmEmailService.beginConfirm(currentUser);
            final List<String> args = Arrays.asList(
                    userEmail,
                ConfirmEmailUtil.friendlyExpirationTime(systemConfig.getMinutesUntilConfirmEmailTokenExpires()));
            JsfHelper.addSuccessMessage(BundleUtil.getStringFromBundle("confirmEmail.submitRequest.success", args));
        } catch (final ConfirmEmailException ex) {
            Logger.getLogger(BuiltinUserPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean showVerifyEmailButton() {
        /**
         * Determines whether the button to send a verification email appears on user page
         */
        if (confirmEmailService.findSingleConfirmEmailDataByUser(currentUser) == null
                && currentUser.getEmailConfirmed() == null) {
            return true;
        }
        return false;
    }

    public boolean isEmailIsVerified() {
        if (currentUser.getEmailConfirmed() != null && confirmEmailService.findSingleConfirmEmailDataByUser(currentUser) == null) {
            return true;
         } else return false;
    }
    
    public boolean isEmailNotVerified() {
        if (currentUser.getEmailConfirmed() == null || confirmEmailService.findSingleConfirmEmailDataByUser(currentUser) != null) {
            return true;
        } else return false;
    }
    
    public boolean isEmailGrandfathered() {
        final ConfirmEmailUtil confirmEmailUtil = new ConfirmEmailUtil();
        if (currentUser.getEmailConfirmed() == confirmEmailUtil.getGrandfatheredTime()) {
            return true;
        } else return false;
    }

}
