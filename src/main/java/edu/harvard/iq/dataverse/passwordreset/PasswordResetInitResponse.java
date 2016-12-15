package edu.harvard.iq.dataverse.passwordreset;

import edu.harvard.iq.dataverse.util.SystemConfig;

public class PasswordResetInitResponse {

    /**
     * @todo Do we really need emailFound? Just check if passwordResetData is
     * null or not instead?
     */
    private final boolean emailFound;
    private String resetUrl;
    private PasswordResetData passwordResetData;

    public PasswordResetInitResponse(final boolean emailFound) {
        this.emailFound = emailFound;
    }

    public PasswordResetInitResponse(final boolean emailFound, final PasswordResetData passwordResetData) {
        this.emailFound = emailFound;
        this.passwordResetData = passwordResetData;
        final String siteUrl = System.getProperty(SystemConfig.SITE_URL);
        this.resetUrl = siteUrl + "/passwordreset.xhtml?token=" + passwordResetData.getToken();
    }

    public boolean isEmailFound() {
        return emailFound;
    }

    public String getResetUrl() {
        return resetUrl;
    }

    public PasswordResetData getPasswordResetData() {
        return passwordResetData;
    }

}
