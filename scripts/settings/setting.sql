--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.setting DROP CONSTRAINT setting_pkey;
DROP TABLE public.setting;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: setting; Type: TABLE; Schema: public; Owner: dvnapp; Tablespace: 
--

CREATE TABLE setting (
    name character varying(255) NOT NULL,
    content text
);


ALTER TABLE setting OWNER TO dvnapp;

--
-- Data for Name: setting; Type: TABLE DATA; Schema: public; Owner: dvnapp
--

INSERT INTO setting VALUES (':AllowSignUp', 'yes');
INSERT INTO setting VALUES (':SignUpUrl', '/dataverseuser.xhtml?editMode=CREATE');
INSERT INTO setting VALUES (':Protocol', 'doi');
INSERT INTO setting VALUES (':DoiProvider', 'EZID');
INSERT INTO setting VALUES (':DoiSeparator', '/');
INSERT INTO setting VALUES (':OAIServerEnabled', 'true');
INSERT INTO setting VALUES (':ApplicationTermsOfUse', '<strong>
    <h3>UAL Dataverse Network Account Terms of Use</h3>
    <h4>Acceptance of Terms</h4>
</strong>
<p>The following terms and conditions govern all use of the University of Alberta Libraries(UAL) Dataverse website
    (http://dataverse.library.ualberta.ca) (the Site) and the services available on or at the Site (taken together, the
    Service). The Service is owned and operated by the UAL. The Service is offered subject to acceptance without
    modification of all of the terms and conditions contained herein (the Terms of Use) and all other operating rules,
    policies and procedures that may be published from time to time on this Site by UAL.</p>
<br />
<p>The Service is available only to individuals who are at least 13 years old or possess legal parental or guardian
    consent. If you do not so qualify, do not attempt to use the Service. UAL may refuse to offer the Service to any
    person or entity at any time and may change its eligibility criteria, at any time, in its sole discretion.</p>
<h4>Modification of Terms of Use</h4>
<p>UAL reserves the right, at its sole discretion, to modify or replace any of the Terms of Use at any time. It is
    your responsibility to check the Terms of Use periodically for changes. Your continued use of the Service following
    the posting of any changes to the Terms of Use constitutes acceptance of those changes.</p>
<h4>Rules and Conduct</h4>
<p>As a condition of use, you promise not to use the Service for any purpose that is prohibited by the Terms of
    Use. For purposes of the Terms of Use, "Content" includes, without limitation, any information, data, text,
    software, scripts, graphics, and interactive features generated, provided, or otherwise made accessible by UAL or
    its partners on or through the Service.</p>
<p>By way of example, and not as a limitation, you shall not (or permit others to) either (a) take any action or
    (b) upload, download, post, submit or otherwise distribute or facilitate distribution of any content using any
    communications service or other service available on or through the Service, that:</p>
<ul>
    <li>
        <p>infringes any patent, trademark, trade secret, copyright, right of publicity or other right of any other
            person or entity;</p>
    </li>
    <li>
        <p>is unlawful, threatening, abusive, harassing, defamatory, libelous, deceptive, fraudulent, invasive of
            another''s privacy, tortious, obscene, offensive, or profane;</p>
    </li>
    <li>
        <p>constitutes unauthorized or unsolicited advertising, junk or bulk e-mail ("spamming");</p>
    </li>
    <li>
        <p>contains software viruses or any other computer codes, files, or programs that are designed or intended
            to disrupt, damage, limit or interfere with the proper function of any software, hardware, or
            telecommunications equipment or to damage or obtain unauthorized access to any system, data or other
            information of UAL or any third party; or</p>
    </li>
</ul>
<p>Additionally, you shall not: (i) take any action that imposes or may impose (as determined by UAL in its sole
    discretion) an unreasonable or disproportionately large load on UAL''s (or its third party providers'')
    infrastructure; (ii) interfere or attempt to interfere with the proper working of the Service or any activities
    conducted on the Service; or (iii) bypass any measures UAL may use to prevent or restrict access to the Service (or
    other accounts, computer systems or networks connected to the Service).</p>
<p>You shall abide by all applicable local, state, national and international laws and regulations.</p>
<h4>Registration</h4>
<p>As a condition to using Services, you are registering with UAL Dataverse Network using your UofA CCID and
    password. You shall provide UAL with accurate, complete, and updated registration information. Failure to do so
    shall constitute a breach of the Terms of Use, which may result in immediate termination of your UAL Dataverse
    Network account. You shall not (i) select or use as a UAL Login Name a name of another person with the intent to
    impersonate that person; (ii) use as a UAL Login Name a name subject to any rights of a person other than you
    without appropriate authorization; or (iii) use as a UAL Screen Name a name that is otherwise offensive, vulgar or
    obscene. UAL reserves the right to refuse registration of, or cancel a UAL Screen Name in its sole discretion. You
    are solely responsible for activity that occurs on your account and shall be responsible for maintaining the
    confidentiality of your password. You shall never use another user''s account without such other user''s express
    permission. You will immediately notify UAL in writing of any unauthorized use of your account, or other account
    related security breach of which you are aware.</p>
<h4>User Submissions</h4>
<p>The Service provides you with the ability to upload, submit, disclose, distribute or otherwise post (hereafter,
    "posting") content (including, without limitation, information, data, and images) to the Service ("User
    Submissions") and to allow other users to do so. The Service provides you with the ability to display, organize, and
    accept content through creating a Dataverse.</p>
<p>The Services also provides you with the ability to post User Submissions to other Dataverses administered by
    third parties ("Third-Party Dataverse Administrator"), subject to the permissions and terms of use required by those
    Third Party Dataverse Administrators. User Submissions posted to third party Dataverse are also subject to curation
    by ("Curated By") the Third-Party Dataverse Administrator for that dataverse.</p>
<p>An important part of the mission of UAL is to acquire and preserve research data and provide access to it. UAL
    intends to create archival versions of the Content that will facilitate preservation, verification, management, and
    use, and to permanently archive these versions of the Content at multiple locations. UAL will use good archival
    practices, as identified by UAL, to retain and preserve Content deposited into the Site. Notwithstanding, you
    acknowledge that UAL and Third-Party Dataverse Administrators will not be liable for any loss of or damage to the
    User Submissions, either in whole or in part.</p>
<p>Please be aware that the UAL does not review data submissions before they are made available to the public, so
    we may only accept data that is publicly distributable. BEFORE YOU CONTRIBUTE DATA TO THE ARCHIVE, YOU MUST ENSURE
    THAT THE DATA MEETS OUR TERMS AND CONDITIONS. If your data does not meet our terms and conditions, the Archive is
    unable to accept your data. You will be held legally and financially responsible for the Archive''s damages if data
    you contribute violates these terms and conditions.</p>
<p>By posting User Submissions on or at the Site, or otherwise through the Service, to your Dataverse or other
    Dataverses, or by allowing others to do so, you make the following representations and warranties to UAL and to the
    administrator of that Dataverse:</p>
<ul>
    <li>
        <p>The User Submissions do not infringe upon the copyrights or other intellectual property rights,
            including, but not limited to patent, trademark, trade secret, copyright, right of publicity or other right
            of any other of any third party.</p>
    </li>
    <li>
        <p>The User Submissions do not violate any laws, including but not limited to laws related to defamation or
            obscenity.</p>
    </li>
    <li>
        <p>You will promptly notify both UAL and the administrator of the dataverse curating the User Submissions
            of any confidentiality, privacy or data protection, licensing, or intellectual property issues regarding the
            User Submissions.</p>
    </li>
    <li>
        <p>The User Submissions do not contains software viruses or any other computer codes, files, or programs
            that are designed or intended to disrupt, damage, limit or interfere with the proper function of any
            software, hardware, or telecommunications equipment or to damage or obtain unauthorized access to any
            system, data or other information of UAL or any third party.</p>
    </li>
    <li>
        <p>You acknowledge that UAL will not be liable for any loss of or damage to the User Submissions, either in
            whole or in part.</p>
    </li>
    <li>
        <p>If the User Submission is subject to IRB review, the User Submissions were given IRB approval</p>
    </li>
</ul>
<p>The Service gives the Dataverse owner the ability to restrict access to User Submissions to specified user
    accounts ("Restricted User Submissions"). Third-Party Dataverses may also choose to restrict certain User
    Submissions in accordance with their own terms of use of use. All other User Submissions shall be deemed
    Unrestricted. By posting Unrestricted User Submissions on or at the Site, or otherwise through the Service, to your
    Dataverse or other Dataverses, or by allowing others to do so, you make the following representations and warranties
    to UAL and to the administrator of that Dataverse:</p>
<ol>
    <li>
        <p>The User Submissions do not contain information that could directly or indirect directly or indirectly,
            identify subject except where the release of such identifying information has no potential for constituting
            an unwarranted invasion of privacy and/or breach of confidentiality. And for all personally-identifiable
            information in the data at least one of following conditions applies:</p>
    </li>
    <ol>
        <li>
            <p>This information has been previously released to the public;</p>
        </li>
        <li>
            <p>this information describes public figures, where the data relates to their public roles or other
                non-sensitive subjects;</p>
        </li>
        <li>
            <p>a sufficient length of time has passed since the collection of the information so that the data can
                be considered "historic";</p>
        </li>
        <li>
            <p>all identified subjects have given explicit informed consent allowing the public release of the
                information in the dataset;</p>
        </li>
        <li>
            <p>all information was collected with an explicit statement concerning the public nature of the data,
                such as information collected for governmental regulatory purposes;</p>
        </li>
        <li>
            <p>or, for federal records (data created by a U.S. federal government agency or under a federal
                contract) only, all identified subjects are deceased and no federal statute explicitly restricts the
                release of the data</p>
        </li>
    </ol>
    <li>
        <p>You give permission and any required licenses to UAL to make the Content available for archiving,
            preservation and access, in keeping with UAL''s mandate. Including, without restriction permission to:</p>
    </li>
    <ol>
        <li>
            <p>To redisseminate copies of the Data Collection in a variety of distribution formats according the
                standard terms of use of UAL DVN</p>
        </li>
        <li>
            <p>To promote and advertise the Data Collection in any publicity (in any form) for the UAL DVN</p>
        </li>
        <li>
            <p>To describe, catalog, validate and document the User Submissions</p>
        </li>
        <li>
            <p>To store, translate, copy or re-format the Data Collection in any way to ensure its future
                preservation and accessibility; improve usability and/or protect respondent confidentiality</p>
        </li>
        <li>
            <p>To incorporate metadata (cataloging information) or documentation in the User Submission into public
                access catalogues</p>
        </li>
    </ol>
</ol>
<p>You shall not provide UAL with any confidential or proprietary information that you desire or are required to
    keep secret. You are solely responsible for the User Submissions you post on or through the Service. UAL does not
    endorse and has no control over any User Submission.</p>
<p>UAL has no obligation to monitor the Site, Service, Content, or User Submissions. UAL may remove any User
    Submission at any time for any reason (including, but not limited to, upon receipt of claims or allegations from
    third parties or authorities relating to such User Submission), or for no reason at all.</p>
<p>You represent and warrant are lawfully entitled and have full authority to license UAL to use the Materials in
    the ways described in these Terms and Conditions; and you are not under any obligation or restriction created by
    law, contract or otherwise that would prevent you from entering into and fully performing these Terms and
    Conditions.</p>
<h4>Termination</h4>
<p>UAL may terminate your access to all or any part of the Service at any time, with or without cause, with or
    without notice, effective immediately. If you wish to terminate your account, you may notify UAL at
    data@ualberta.ca. All provisions of the Terms of Use which by their nature should survive termination shall
    survive termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity and
    limitations of liability.</p>
<h4>No Warranties</h4>
<p>THE SERVICE (INCLUDING, WITHOUT LIMITATION, THE CONTENT AND USER SUBMISSIONS) IS PROVIDED "AS IS" AND "AS
    AVAILABLE" AND IS WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
    WARRANTIES OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND ANY WARRANTIES
    IMPLIED BY ANY COURSE OF PERFORMANCE OR USAGE OF TRADE, ALL OF WHICH ARE EXPRESSLY DISCLAIMED. UAL, AND ITS
    DIRECTORS, EMPLOYEES, AGENTS, SUPPLIERS, PARTNERS AND CONTENT PROVIDERS DO NOT WARRANT THAT: (A) THE CONTENT OR USER
    SUBMISSIONS ARE TIMELY, ACCURATE, COMPLETE, RELIABLE OR CORRECT; (B) THE SERVICE WILL BE SECURE OR AVAILABLE AT ANY
    PARTICULAR TIME OR LOCATION; (C) ANY DEFECTS OR ERRORS WILL BE CORRECTED; (D) THE CONTENT OR USER SUBMISSIONS ARE
    FREE OF VIRUSES OR OTHER HARMFUL COMPONENTS; OR (E) THE RESULTS OF USING THE SERVICE WILL MEET YOUR REQUIREMENTS.
    YOUR USE OF THE SERVICE IS SOLELY AT YOUR OWN RISK.</p>
<p>UAL MAKES NO WARRANTIES, EXPRESS OR IMPLIED, BY OPERATION OF LAW OR OTHERWISE, REGARDING OR RELATING TO THE
    SERVICES OR CONTENT PROVIDED.</p>
<h4>Limitation of Liability</h4>
<p>IN NO EVENT SHALL UAL, NOR ITS DIRECTORS, EMPLOYEES, AGENTS, PARTNERS, SUPPLIERS OR CONTENT PROVIDERS, BE LIABLE
    UNDER CONTRACT, TORT, STRICT LIABILITY, NEGLIGENCE OR ANY OTHER LEGAL THEORY WITH RESPECT TO THE SERVICE OR ANY
    CONTENT OR USER SUBMISSIONS (I) FOR ANY LOST PROFITS OR SPECIAL, INDIRECT, INCIDENTAL, PUNITIVE, OR CONSEQUENTIAL
    DAMAGES OF ANY KIND WHATSOEVER, SUBSTITUTE GOODS OR SERVICES (HOWEVER ARISING), (II) FOR ANY BUGS, VIRUSES, TROJAN
    HORSES, OR THE LIKE (REGARDLESS OF THE SOURCE OF ORIGINATION), (III) FOR ANY ERRORS OR OMISSIONS IN ANY CONTENT OR
    USER SUBMISSIONS OR FOR ANY LOSS OR DAMAGE OF ANY KIND INCURRED AS A RESULT OF YOUR USE OF ANY CONTENT OR USER
    SUBMISSIONS POSTED, EMAILED, TRANSMITTED OR OTHERWISE MADE AVAILABLE ON OR THROUGH THE SERVICE, OR (IV) FOR ANY
    DIRECT DAMAGES IN EXCESS OF (IN THE AGGREGATE) $10 (U.S.). SOME STATES DO NOT ALLOW THE EXCLUSION OR LIMITATION OF
    INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE ABOVE LIMITATIONS AND EXCLUSIONS MAY NOT APPLY TO YOU.</p>
<h4>Indemnification</h4>
<p>You will indemnify and hold UAL and Third-Party Dataverse Administrators harmless from and against any and all
    loss, cost, expense, liability, or damage, including, without limitation, all reasonable attorneys'' fees and court
    costs, arising from the i) use or misuse of the Service; (ii) your access to the Site, use of the Services,
    violation of the Terms of Use by you; or, (iii) the infringement by you, or any third party using your account, of
    any intellectual property or other right of any person or entity. Such losses, costs, expenses, damages, or
    liabilities shall include, without limitation, all actual, general, special, and consequential damages.</p>
<h4>Dispute Resolution</h4>
<p>A printed version of the Terms of Use and of any notice given in electronic form shall be admissible in judicial
    or administrative proceedings based upon or relating to the Terms of Use to the same extent and subject to the same
    conditions as other business documents and records originally generated and maintained in printed form. You and UAL
    agree that any cause of action arising out of or related to the Service must commence within one (1) year after the
    cause of action arose; otherwise, such cause of action is permanently barred. These Terms and Conditions shall be
    governed by and interpreted in accordance with the University policies. All disputes under these Terms and
    Conditions will be resolved according to the University''s rules and procedures expressed in codes of conduct or
    formal agreements.</p>
<h4>Integration and Severability</h4>
<p>The Terms of Use are the entire agreement between you and UAL with respect to the Service and use of this Site,
    and supersede all prior or contemporaneous communications and proposals (whether oral, written or electronic)
    between you and UAL with respect to this Site (but excluding the use of any software which may be subject to a
    separate end-user license agreement). If any provision of the Terms of Use is found to be unenforceable or invalid,
    that provision will be limited or eliminated to the minimum extent necessary so that the Terms of Use will otherwise
    remain in full force and effect and enforceable.</p>
<h4>Miscellaneous</h4>
<p>The Terms of Use are personal to you, and are not assignable, transferable or sublicensable by you except with
    UAL''s prior written consent. UAL may assign, transfer or delegate any of its rights and obligations hereunder
    without consent. No agency, partnership, joint venture, or employment relationship is created as a result of the
    Terms of Use and neither party has any authority of any kind to bind the other in any respect. In any action or
    proceeding to enforce rights under the Terms of Use, the prevailing party will be entitled to recover costs and
    attorneys'' fees. All notices under the Terms of Use will be in writing and will be deemed to have been duly given
    when received, if personally delivered or sent by certified or registered mail, return receipt requested; when
    receipt is electronically confirmed, if transmitted by facsimile or e-mail; or the day after it is sent, if sent for
    next day delivery by recognized overnight delivery service.</p>
<h4>Copyright and Trademark Notices</h4>
<p>You acknowledge that the copyright in any additional data added by UAL or Third Party Dataverse Administrators to
    the user materials, and any search software, user guides, documentation and any other intellectual property that is
    prepared by UAL or by Third Party Dataverse Administrators to assist users in using the User Submissions will belong
    to UAL or the Third Party Dataverse Administrator(s) creating the content (respectively).</p>
<p>The Dataverse Network and Dataverse Network logo are either trademarks or registered trademarks of IQSS.</p>
<p>This Terms of Use document is available under a Creative Commons Attribution-ShareAlike 2.5 License. It
    incorporates portions originally developed by Swivel, LLC, and the Institute for Quantitative Social Science at
    Harvard University.</p>
<br />
<br />
<p>BY SIGNING MY NAME IN THE SPACE LABELED "SIGNATURE" BELOW AND BY INITIALING THE "I AGREE" BOX BELOW, I CONFIRM
    (A) THAT I HAVE READ AND UNDERSTOOD EACH AND EVERY TERM SET FORTH ABOVE, (B) THE ABOVE REPRESENTATIONS AND THE
    INFORMATION I PROVIDE BELOW ARE ACCURATE, AND (C) I AGREE TO BE BOUND BY THE ABOVE TERMS AND CONDITIONS.</p>');
INSERT INTO setting VALUES (':ApplicationPrivacyPolicyUrl', '/dataverse_terms_of_use.html');
INSERT INTO setting VALUES (':FooterCopyright', ' University of Alberta Libraries | University of Alberta, Edmonton, AB, Canada T6G 2R3');
INSERT INTO setting VALUES (':MaxFileUploadSizeInBytes', '2147483648');
INSERT INTO setting VALUES (':TabularIngestSizeLimit', '2000000000');
INSERT INTO setting VALUES (':ApiTermsOfUse', '/dataverse_terms_of_use.html');
INSERT INTO setting VALUES (':BlockedApiPolicy', 'localhost-only');
INSERT INTO setting VALUES (':BlockedApiEndpoints', 'admin,builtin-users');
INSERT INTO setting VALUES (':ShibEnabled', 'true');
INSERT INTO setting VALUES (':TwoRavensTabularView', 'false');
INSERT INTO setting VALUES (':Authority', '10.7939/DVN');
INSERT INTO setting VALUES (':SystemEmail', 'UAL Dataverse <data@ualberta.ca>');


--
-- Name: setting_pkey; Type: CONSTRAINT; Schema: public; Owner: dvnapp; Tablespace: 
--

ALTER TABLE ONLY setting
    ADD CONSTRAINT setting_pkey PRIMARY KEY (name);


--
-- PostgreSQL database dump complete
--

