package edu.harvard.iq.dataverse.repositorystorageabstractionlayer;

import edu.harvard.iq.dataverse.Dataset;
import edu.harvard.iq.dataverse.FileMetadata;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RepositoryStorageAbstractionLayerUtilTest {

    @Test
    public void testGetRsalSites_3args() {
        System.out.println("getRsalSites");
        Dataset dataset = new Dataset();
        dataset.setIdentifier("identifierPartOfPersistentID");
        dataset.setAuthority("10.5072/FK2");
        String replicationSitesInDB = "dv.sbgrid.org:Harvard Medical School:USA,sbgrid.icm.uu.se:Uppsala University:Sweden,sbgrid.ncpss.org:Institut Pasteur de Montevideo:Uruguay,sbgrid.ncpss.org:Shanghai Institutes for Biological Sciences:China";
        JsonArray myList = RepositoryStorageAbstractionLayerUtil.getSitesFromDb(replicationSitesInDB);
        List<RsyncSite> result = RepositoryStorageAbstractionLayerUtil.getRsyncSites(dataset, myList);
        System.out.println(result.get(0).getName());
        assertEquals("Harvard Medical School", result.get(0).getName());
        assertEquals("USA", result.get(0).getCountry());
        assertEquals("dv.sbgrid.org", result.get(0).getFqdn());
        assertEquals("10.5072/FK2/identifierPartOfPersistentID", result.get(0).getFullRemotePathToDirectory());
        assertEquals("rsync -av rsync://dv.sbgrid.org/10.5072/FK2/identifierPartOfPersistentID", result.get(0).getRsyncDownloadcommand());
    }

    @Test
    public void testGetRsalSites_String() {
        System.out.println("getRsalSites");
        String replicationSitesInDB = "dv.sbgrid.org:Harvard Medical School:USA,sbgrid.icm.uu.se:Uppsala University:Sweden,sbgrid.ncpss.org:Institut Pasteur de Montevideo:Uruguay,sbgrid.ncpss.org:Shanghai Institutes for Biological Sciences:China";
        JsonArray result = RepositoryStorageAbstractionLayerUtil.getSitesFromDb(replicationSitesInDB);
        JsonObject first = (JsonObject) result.get(0);
        System.out.println(result);
        assertEquals("Harvard Medical School", first.getString("name"));
    }

    @Test
    public void testGetLocalDataAccessDirectory() {
        System.out.println("getLocalDataAccessDirectory");
        String localDataAccessParentDir = "/opt/data";
        Dataset dataset = new Dataset();
        dataset.setIdentifier("identifierPartOfPersistentID");
        dataset.setAuthority("10.5072/FK2");
        String result = RepositoryStorageAbstractionLayerUtil.getLocalDataAccessDirectory(localDataAccessParentDir, dataset);
        assertEquals("/opt/data/10.5072/FK2/identifierPartOfPersistentID", result);
    }

    @Test
    public void testGetVerifyDataCommand() {
        System.out.println("getVerifyDataCommand");
        Dataset dataset = new Dataset();
        dataset.setIdentifier("identifierPartOfPersistentID");
        String result = RepositoryStorageAbstractionLayerUtil.getVerifyDataCommand(dataset);
        assertEquals("cd identifierPartOfPersistentID ; shasum -c files.sha", result);
    }

}
