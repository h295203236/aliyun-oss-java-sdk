package com.aliyun.oss.common.parser;

import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.internal.ResponseParsers;
import com.aliyun.oss.model.*;
import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoufeng.chen on 2018/1/10.
 */
public class ResponseParsersTest {
    @Test
    public void testParseGetBucketReplicationWithCloudLocation() {
        String respBody = "<ReplicationConfiguration>\n" +
                " <Rule>\n" +
                "    <ID>12345678</ID>\n" +
                "        <Destination>\n" +
                "            <Bucket>testBucketName</Bucket>\n" +
                "            <Cloud>testCloud</Cloud>\n" +
                "            <CloudLocation>testCloudLocation</CloudLocation>\n" +
                "        </Destination>\n" +
                "    <Status>doing</Status>\n" +
                "    <HistoricalObjectReplication>enabled</HistoricalObjectReplication>\n" +
                " </Rule>\n" +
                "</ReplicationConfiguration>\n";
        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<ReplicationRule> rules = null;
        try {
            rules = ResponseParsers.parseGetBucketReplication(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }
        Assert.assertTrue(rules.size() > 0);

        ReplicationRule rule = rules.get(0);
        Assert.assertEquals("12345678", rule.getReplicationRuleID());
        Assert.assertEquals("testBucketName", rule.getTargetBucketName());
        Assert.assertNull(rule.getTargetBucketLocation());
        Assert.assertEquals("testCloud", rule.getTargetCloud());
        Assert.assertEquals("testCloudLocation", rule.getTargetCloudLocation());
        Assert.assertEquals(true, rule.isEnableHistoricalObjectReplication());
    }

    @Test
    public void testParseGetBucketReplicationWithoutCloudLocation() {
        String respBody = "<ReplicationConfiguration>\n" +
                " <Rule>\n" +
                "    <ID>12345678</ID>\n" +
                "        <Destination>\n" +
                "            <Bucket>testBucketName</Bucket>\n" +
                "            <Location>testLocation</Location>\n" +
                "        </Destination>\n" +
                "    <Status>doing</Status>\n" +
                "    <HistoricalObjectReplication>disabled</HistoricalObjectReplication>\n" +
                " </Rule>\n" +
                "</ReplicationConfiguration>\n";
        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<ReplicationRule> rules = null;
        try {
            rules = ResponseParsers.parseGetBucketReplication(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }
        Assert.assertTrue(rules.size() > 0);

        ReplicationRule rule = rules.get(0);
        Assert.assertEquals("12345678", rule.getReplicationRuleID());
        Assert.assertEquals("testBucketName", rule.getTargetBucketName());
        Assert.assertEquals("testLocation", rule.getTargetBucketLocation());
        Assert.assertNull(rule.getTargetCloud());
        Assert.assertNull(rule.getTargetCloudLocation());
        Assert.assertEquals(false, rule.isEnableHistoricalObjectReplication());
    }

    @Test
    public void testParseGetBucketReplicationProgressWithCloudLocation() {
        Date dt = new Date();
        String respBody = "<ReplicationProgress>\n" +
                " <Rule>\n" +
                "     <ID>12345678</ID>\n" +
                "     <Destination>\n" +
                "         <Bucket>testBucketName</Bucket>\n" +
                "         <Cloud>testCloud</Cloud>\n" +
                "         <CloudLocation>testCloudLocation</CloudLocation>\n" +
                "     </Destination>\n" +
                "     <PrefixSet>\n" +
                "         <Prefix>aaa</Prefix>\n" +
                "         <Prefix>bbb</Prefix>\n" +
                "     </PrefixSet>\n" +
                "     <Action>xxx,xxx,xxx</Action>\n" +
                "     <Status>doing</Status>\n" +
                "     <HistoricalObjectReplication>enabled</HistoricalObjectReplication>\n" +
                "     <Progress>\n" +
                "         <HistoricalObject>0.8</HistoricalObject>\n" +
                "         <NewObject>" + DateUtil.formatIso8601Date(dt) + "</NewObject>\n" +
                "     </Progress>\n" +
                " </Rule>\n" +
                "</ReplicationProgress>";


        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        BucketReplicationProgress progress = null;
        try {
            progress = ResponseParsers.parseGetBucketReplicationProgress(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication process response body fail!");
        }

        Assert.assertEquals("12345678", progress.getReplicationRuleID());
        Assert.assertEquals("testBucketName", progress.getTargetBucketName());
        Assert.assertNull(progress.getTargetBucketLocation());
        Assert.assertEquals("testCloud", progress.getTargetCloud());
        Assert.assertEquals("testCloudLocation", progress.getTargetCloudLocation());
        Assert.assertEquals(0.8f, progress.getHistoricalObjectProgress());
        Assert.assertEquals(dt, progress.getNewObjectProgress());

    }

    @Test
    public void testParseGetBucketReplicationProgressWithoutCloudLocation() {
        Date dt = new Date();
        String respBody = "<ReplicationProgress>\n" +
                " <Rule>\n" +
                "     <ID>12345678</ID>\n" +
                "     <Destination>\n" +
                "         <Bucket>testBucketName</Bucket>\n" +
                "         <Location>testLocation</Location>\n" +
                "     </Destination>\n" +
                "     <PrefixSet>\n" +
                "         <Prefix>aaa</Prefix>\n" +
                "         <Prefix>bbb</Prefix>\n" +
                "     </PrefixSet>\n" +
                "     <Action>xxx,xxx,xxx</Action>\n" +
                "     <Status>doing</Status>\n" +
                "     <HistoricalObjectReplication>enabled</HistoricalObjectReplication>\n" +
                "     <Progress>\n" +
                "         <HistoricalObject>0.9</HistoricalObject>\n" +
                "         <NewObject>" + DateUtil.formatIso8601Date(dt) + "</NewObject>\n" +
                "     </Progress>\n" +
                " </Rule>\n" +
                "</ReplicationProgress>";


        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        BucketReplicationProgress progress = null;
        try {
            progress = ResponseParsers.parseGetBucketReplicationProgress(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication process response body fail!");
        }

        Assert.assertEquals("12345678", progress.getReplicationRuleID());
        Assert.assertEquals("testBucketName", progress.getTargetBucketName());
        Assert.assertEquals("testLocation", progress.getTargetBucketLocation());
        Assert.assertNull(progress.getTargetCloud());
        Assert.assertNull(progress.getTargetCloudLocation());
        Assert.assertEquals(0.9f, progress.getHistoricalObjectProgress());
        Assert.assertEquals(dt, progress.getNewObjectProgress());
    }

    @Test
    public void testParseGetBucketReplicationWithSyncRole() {
        String respBody = "<ReplicationConfiguration>\n" +
                " <Rule>\n" +
                "    <ID>12345678</ID>\n" +
                "        <Destination>\n" +
                "            <Bucket>testBucketName</Bucket>\n" +
                "            <Cloud>testCloud</Cloud>\n" +
                "            <CloudLocation>testCloudLocation</CloudLocation>\n" +
                "        </Destination>\n" +
                "    <Status>doing</Status>\n" +
                "    <HistoricalObjectReplication>enabled</HistoricalObjectReplication>\n" +
                "    <SyncRole>ft-sync-role</SyncRole>\n" +
                " </Rule>\n" +
                "</ReplicationConfiguration>\n";
        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<ReplicationRule> rules = null;
        try {
            rules = ResponseParsers.parseGetBucketReplication(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }
        Assert.assertTrue(rules.size() > 0);

        ReplicationRule rule = rules.get(0);
        Assert.assertEquals("12345678", rule.getReplicationRuleID());
        Assert.assertEquals("testBucketName", rule.getTargetBucketName());
        Assert.assertNull(rule.getTargetBucketLocation());
        Assert.assertEquals("testCloud", rule.getTargetCloud());
        Assert.assertEquals("testCloudLocation", rule.getTargetCloudLocation());
        Assert.assertEquals(true, rule.isEnableHistoricalObjectReplication());
        Assert.assertEquals("ft-sync-role", rule.getSyncRole());
        Assert.assertNull(rule.getSseKmsEncryptedObjectsStatus());
        Assert.assertNull(rule.getReplicaKmsKeyID());
    }

    @Test
    public void testParseGetBucketReplicationWithReplicaKmsKeyID() {
        String respBody = "<ReplicationConfiguration>\n" +
                " <Rule>\n" +
                "    <ID>12345678</ID>\n" +
                "        <Destination>\n" +
                "            <Bucket>testBucketName</Bucket>\n" +
                "            <Cloud>testCloud</Cloud>\n" +
                "            <CloudLocation>testCloudLocation</CloudLocation>\n" +
                "        </Destination>\n" +
                "    <Status>doing</Status>\n" +
                "    <HistoricalObjectReplication>enabled</HistoricalObjectReplication>\n" +
                "    <EncryptionConfiguration>\n" +
                "        <ReplicaKmsKeyID>12345</ReplicaKmsKeyID>\n" +
                "    </EncryptionConfiguration>\n" +
                " </Rule>\n" +
                "</ReplicationConfiguration>\n";
        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<ReplicationRule> rules = null;
        try {
            rules = ResponseParsers.parseGetBucketReplication(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }
        Assert.assertTrue(rules.size() > 0);

        ReplicationRule rule = rules.get(0);
        Assert.assertEquals("12345678", rule.getReplicationRuleID());
        Assert.assertEquals("testBucketName", rule.getTargetBucketName());
        Assert.assertNull(rule.getTargetBucketLocation());
        Assert.assertEquals("testCloud", rule.getTargetCloud());
        Assert.assertEquals("testCloudLocation", rule.getTargetCloudLocation());
        Assert.assertEquals(true, rule.isEnableHistoricalObjectReplication());
        Assert.assertNull(rule.getSyncRole());
        Assert.assertNull(rule.getSseKmsEncryptedObjectsStatus());
        Assert.assertEquals("12345", rule.getReplicaKmsKeyID());
    }

    @Test
    public void testParseGetBucketReplicationWithSseKmsEncryptedObjectsStatus() {
        String respBody = "<ReplicationConfiguration>\n" +
                " <Rule>\n" +
                "    <ID>12345678</ID>\n" +
                "        <Destination>\n" +
                "            <Bucket>testBucketName</Bucket>\n" +
                "            <Cloud>testCloud</Cloud>\n" +
                "            <CloudLocation>testCloudLocation</CloudLocation>\n" +
                "        </Destination>\n" +
                "    <Status>doing</Status>\n" +
                "    <HistoricalObjectReplication>enabled</HistoricalObjectReplication>\n" +
                "    <SourceSelectionCriteria>\n" +
                "         <SseKmsEncryptedObjects>\n" +
                "             <Status>Enabled</Status>\n" +
                "         </SseKmsEncryptedObjects>\n" +
                "    </SourceSelectionCriteria>\n" +
                " </Rule>\n" +
                "</ReplicationConfiguration>\n";
        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<ReplicationRule> rules = null;
        try {
            rules = ResponseParsers.parseGetBucketReplication(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }
        Assert.assertTrue(rules.size() > 0);

        ReplicationRule rule = rules.get(0);
        Assert.assertEquals("12345678", rule.getReplicationRuleID());
        Assert.assertEquals("testBucketName", rule.getTargetBucketName());
        Assert.assertNull(rule.getTargetBucketLocation());
        Assert.assertEquals("testCloud", rule.getTargetCloud());
        Assert.assertEquals("testCloudLocation", rule.getTargetCloudLocation());
        Assert.assertEquals(true, rule.isEnableHistoricalObjectReplication());
        Assert.assertNull(rule.getSyncRole());
        Assert.assertEquals("Enabled", rule.getSseKmsEncryptedObjectsStatus());
        Assert.assertNull(rule.getReplicaKmsKeyID());
    }

    @Test
    public void testParseGetLiveChannelStat() {
        String respBody = "" +
                "<LiveChannelStat>\n" +
                "  <Status>Live</Status>\n" +
                "  <ConnectedTime>2016-08-25T06:25:15.000Z</ConnectedTime>\n" +
                "  <RemoteAddr>10.1.2.3:47745</RemoteAddr>\n" +
                "  <Video>\n" +
                "    <Width>1280</Width>\n" +
                "    <Height>536</Height>\n" +
                "    <FrameRate>24</FrameRate>\n" +
                "    <Bandwidth>0</Bandwidth>\n" +
                "    <Codec>H264</Codec>\n" +
                "  </Video>\n" +
                "  <Audio>\n" +
                "    <Bandwidth>0</Bandwidth>\n" +
                "    <SampleRate>44100</SampleRate>\n" +
                "    <Codec>ADPCM</Codec>\n" +
                "  </Audio>\n" +
                "</LiveChannelStat>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        LiveChannelStat stat = null;
        try {
            stat = ResponseParsers.parseGetLiveChannelStat(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals(PushflowStatus.Live, stat.getPushflowStatus());
        Assert.assertEquals("10.1.2.3:47745", stat.getRemoteAddress());
        Assert.assertEquals(1280, stat.getVideoStat().getWidth());
        Assert.assertEquals(44100, stat.getAudioStat().getSampleRate());
    }

    @Test
    public void testParseGetLiveChannelHistory() {
        String respBody = "" +
                "<LiveChannelHistory>\n" +
                "  <LiveRecord>\n" +
                "    <StartTime>2016-07-30T01:53:21.000Z</StartTime>\n" +
                "    <EndTime>2016-07-30T01:53:31.000Z</EndTime>\n" +
                "    <RemoteAddr>10.101.194.148:56861</RemoteAddr>\n" +
                "  </LiveRecord>\n" +
                "  <LiveRecord>\n" +
                "    <StartTime>2016-07-30T01:53:35.000Z</StartTime>\n" +
                "    <EndTime>2016-07-30T01:53:45.000Z</EndTime>\n" +
                "    <RemoteAddr>10.101.194.148:57126</RemoteAddr>\n" +
                "  </LiveRecord>\n" +
                "</LiveChannelHistory>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<LiveRecord> records = null;
        try {
            records = ResponseParsers.parseGetLiveChannelHistory(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals(2, records.size());
        Assert.assertEquals("10.101.194.148:56861", records.get(0).getRemoteAddress());
    }

    @Test
    public void testParseListImageStyle() {
        String respBody = "" +
                "<ImageStyle>\n" +
                "  <Style>\n" +
                "    <Name>Name1</Name>\n" +
                "    <Content>Style1</Content>\n" +
                "    <LastModifyTime>Wed, 02 Oct 2019 14:30:18 GMT</LastModifyTime>\n" +
                "    <CreateTime>Wed, 02 Oct 2019 14:30:18 GMT</CreateTime>\n" +
                "  </Style>\n" +
                "  <Style>\n" +
                "    <Name>Name2</Name>\n" +
                "    <Content>Style2</Content>\n" +
                "    <LastModifyTime>Wed, 02 Oct 2019 14:30:18 GMT</LastModifyTime>\n" +
                "    <CreateTime>Wed, 02 Oct 2019 14:30:18 GMT</CreateTime>\n" +
                "  </Style>\n" +
                "</ImageStyle>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<Style> records = null;
        try {
            records = ResponseParsers.parseListImageStyle(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals(2, records.size());
        Assert.assertEquals("Name1", records.get(0).GetStyleName());
    }

    @Test
    public void testParseBucketImage() {
        String respBody = "" +
                "  <Style>\n" +
                "    <Name>Name</Name>\n" +
                "    <Default404Pic>404Pic</Default404Pic>\n" +
                "    <StyleDelimiters>#</StyleDelimiters>\n" +
                "    <Status>Enable</Status>\n" +
                "    <AutoSetContentType>True</AutoSetContentType>\n" +
                "    <OrigPicForbidden>True</OrigPicForbidden>\n" +
                "    <SetAttachName>True</SetAttachName>\n" +
                "    <UseStyleOnly>True</UseStyleOnly>\n" +
                "    <UseSrcFormat>True</UseSrcFormat>\n" +
                "  </Style>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        GetBucketImageResult result = null;
        try {
            result = ResponseParsers.parseBucketImage(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals("Enable", result.GetStatus());
    }

    @Test
    public void testParseImageStyle() {
        String respBody = "" +
                "  <Style>\n" +
                "    <Name>Name1</Name>\n" +
                "    <Content>Style1</Content>\n" +
                "    <LastModifyTime>Wed, 02 Oct 2019 14:30:18 GMT</LastModifyTime>\n" +
                "    <CreateTime>Wed, 02 Oct 2019 14:30:18 GMT</CreateTime>\n" +
                "  </Style>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        GetImageStyleResult result = null;
        try {
            result = ResponseParsers.parseImageStyle(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals("Name1", result.GetStyleName());
    }

    @Test
    public void testparseGetBucketCname() {
        String respBody = "" +
                "<CnameConfiguration>\n" +
                "  <Cname>\n" +
                "    <Domain>Domain1</Domain>\n" +
                "    <Status>Enabled</Status>\n" +
                "    <LastModified>2019-09-30T01:53:45.000Z</LastModified>\n" +
                "    <IsPurgeCdnCache>True</IsPurgeCdnCache>\n" +
                "  </Cname>\n " +
                "  <Cname>\n" +
                "    <Domain>Domain2</Domain>\n" +
                "    <Status>Disabled</Status>\n" +
                "    <LastModified>2019-09-30T01:53:45.000Z</LastModified>\n" +
                "  </Cname>\n" +
                "</CnameConfiguration>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        List<CnameConfiguration> result = null;
        try {
            result = ResponseParsers.parseGetBucketCname(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(new Boolean(true), result.get(0).getPurgeCdnCache());
        Assert.assertEquals(null, result.get(1).getPurgeCdnCache());
    }

    @Test
    public void testParseGetBucketInfo() {
        String respBody = "" +
                "<BucketInfo>\n" +
                "  <Bucket>\n" +
                "           <CreationDate>2013-07-31T10:56:21.000Z</CreationDate>\n" +
                "            <ExtranetEndpoint>oss-cn-hangzhou.aliyuncs.com</ExtranetEndpoint>\n" +
                "            <IntranetEndpoint>oss-cn-hangzhou-internal.aliyuncs.com</IntranetEndpoint>\n" +
                "            <Location>oss-cn-hangzhou</Location>\n" +
                "            <Name>oss-example</Name>\n" +
                "            <Owner>\n" +
                "              <DisplayName>username</DisplayName>\n" +
                "              <ID>27183473914****</ID>\n" +
                "            </Owner>\n" +
                "            <AccessControlList>\n" +
                "              <Grant>private</Grant>\n" +
                "            </AccessControlList>\n" +
                "            <Comment>test</Comment>\n" +
                "          </Bucket>\n" +
                " </BucketInfo>";

        InputStream instream = null;
        try {
            instream = new ByteArrayInputStream(respBody.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("UnsupportedEncodingException");
        }

        BucketInfo result = null;
        try {
            result = ResponseParsers.parseGetBucketInfo(instream);
        } catch (ResponseParseException e) {
            e.printStackTrace();
            Assert.fail("parse bucket replication response body fail!");
        }

        Assert.assertEquals("test", result.getComment());
        Assert.assertEquals(CannedAccessControlList.Private, result.getCannedACL());
        Assert.assertEquals("oss-cn-hangzhou", result.getBucket().getLocation());
        Assert.assertEquals("oss-example", result.getBucket().getName());
    }
}
