---
name: Release request
about: Create a request for a new release

---

**Tag**
The label of the tag to be released, and link to release in [IQSS/Dataverse release list](https://github.com/IQSS/dataverse/releases).

**Test checklist**
Perform the following tests on the staging server.

- [ ] Shibboleth login (testing accounts like Henry’s which is linked to ccid but identifier is not e-mail, vs. Peter’s which does),
- [ ] Basic searches
- [ ] Dataverse creation
- [ ] Dataset uploads & downloads
- [ ] Dataset versioning
- [ ] Testing views as a function of different permission settings (e.g., standard user, Dataverse administrator, Dataverse Systems Administrator)
- [ ] Terms of Use feature
- [ ] Guestbook feature
- [ ] OAI List records: ```/oai?verb=ListRecords&metadataPrefix=oai_ddi``` (note: the default set may not be built immediately). Should return set of XML DDI records.
- [ ] Metadata check

**Notes**
Any other information about this release, e.g.

- new features (which may require decisions by the service manager)
- content changes (which may require extra attention to checking metadata)
