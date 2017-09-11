package com.floragunn.searchguard;

import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.Test;

import com.floragunn.searchguard.support.ConfigConstants;
import com.floragunn.searchguard.test.DynamicSgConfig;
import com.floragunn.searchguard.test.SingleClusterTest;
import com.floragunn.searchguard.test.helper.cluster.ClusterConfiguration;
import com.floragunn.searchguard.test.helper.rest.RestHelper;
import com.floragunn.searchguard.test.helper.rest.RestHelper.HttpResponse;

public class StandardIntegrationTests extends SingleClusterTest {
    
    @Test
    public void testDefaultInit() throws Exception {
        
        Settings b = Settings.builder().put(ConfigConstants.SEARCHGUARD_ALLOW_DEFAULT_INIT_SGINDEX, true).build();
        setup(Settings.EMPTY, new DynamicSgConfig(), b, false);
        
        RestHelper rh = nonSslRestHelper();
        HttpResponse res;
        Thread.sleep(5000);
        Assert.assertEquals(HttpStatus.SC_OK, (res = rh.executeGetRequest("_searchguard/license?pretty", encodeBasicHeader("admin", "admin"))).getStatusCode());
        System.out.println(res.getBody());
        assertContains(res, "*TRIAL*");
        assertNotContains(res, "*FULL*");
    }
    
    @Test
    public void testTrialLicense() throws Exception {
        setup(Settings.EMPTY, new DynamicSgConfig(), Settings.EMPTY);
        
        RestHelper rh = nonSslRestHelper();
        HttpResponse res;
        Assert.assertEquals(HttpStatus.SC_OK, (res = rh.executeGetRequest("_searchguard/license?pretty")).getStatusCode());
        System.out.println(res.getBody());
        assertContains(res, "*TRIAL*");
        assertNotContains(res, "*FULL*");
    }
    
    @Test
    public void testFullLicense() throws Exception {
        setup(Settings.EMPTY, new DynamicSgConfig().setSgConfig("sg_config_lic.yml"), Settings.EMPTY);
        
        RestHelper rh = nonSslRestHelper();
        HttpResponse res;
        Assert.assertEquals(HttpStatus.SC_OK, (res = rh.executeGetRequest("_searchguard/license?pretty", encodeBasicHeader("nagilum", "nagilum"))).getStatusCode());
        System.out.println(res.getBody());
        Assert.assertTrue(res.getBody().contains("FULL"));
        Assert.assertTrue(res.getBody().contains("is_valid\" : true"));
    }
    
    @Test
    public void testHTTPBasic() throws Exception {

        /*final Settings settings = Settings.builder().put("searchguard.ssl.transport.enabled", true)
                .put(SSLConfigConstants.SEARCHGUARD_SSL_HTTP_ENABLE_OPENSSL_IF_AVAILABLE, allowOpenSSL)
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLE_OPENSSL_IF_AVAILABLE, allowOpenSSL)
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_ALIAS, "node-0")
                .put("searchguard.ssl.transport.keystore_filepath", getAbsoluteFilePathFromClassPath("node-0-keystore.jks"))
                .put("searchguard.ssl.transport.truststore_filepath", getAbsoluteFilePathFromClassPath("truststore.jks"))
                .put("searchguard.ssl.transport.enforce_hostname_verification", false)
                .put("searchguard.ssl.transport.resolve_hostname", false)
                .putArray("searchguard.authcz.admin_dn", "CN=kirk,OU=client,O=client,l=tEst, C=De")
                .putArray("searchguard.authcz.impersonation_dn.CN=spock,OU=client,O=client,L=Test,C=DE", "worf")
                .build();*/
        
        setup(Settings.EMPTY, new DynamicSgConfig(), Settings.EMPTY);

        /*Settings tcSettings = Settings.builder().put("cluster.name", clustername)
                .put(settings)
                .put("searchguard.ssl.transport.keystore_filepath", getAbsoluteFilePathFromClassPath("kirk-keystore.jks"))
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_ALIAS,"kirk")
                .put("path.home", ".").build();*/

        try (TransportClient tc = getInternalTransportClient(this.clusterInfo, Settings.EMPTY)) {
            
            tc.admin().indices().create(new CreateIndexRequest("copysf")).actionGet();
            
            tc.index(new IndexRequest("vulcangov").type("kolinahr").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
           // tc.index(new IndexRequest("vulcangov").type("secrets").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
           // tc.index(new IndexRequest("vulcangov").type("planet").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            
            tc.index(new IndexRequest("starfleet").type("ships").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            // tc.index(new IndexRequest("starfleet").type("captains").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            // tc.index(new IndexRequest("starfleet").type("public").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            
            tc.index(new IndexRequest("starfleet_academy").type("students").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            // tc.index(new IndexRequest("starfleet_academy").type("alumni").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            
            tc.index(new IndexRequest("starfleet_library").type("public").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            // tc.index(new IndexRequest("starfleet_library").type("administration").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            
            tc.index(new IndexRequest("klingonempire").type("ships").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            // tc.index(new IndexRequest("klingonempire").type("praxis").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            
            tc.index(new IndexRequest("public").type("legends").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            //tc.index(new IndexRequest("public").type("hall_of_fame").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            // tc.index(new IndexRequest("public").type("hall_of_fame").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":2}", XContentType.JSON)).actionGet();

            tc.index(new IndexRequest("spock").type("type01").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            tc.index(new IndexRequest("kirk").type("type01").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();
            tc.index(new IndexRequest("role01_role02").type("type01").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":1}", XContentType.JSON)).actionGet();

            tc.admin().indices().aliases(new IndicesAliasesRequest().addAliasAction(AliasActions.add().indices("starfleet","starfleet_academy","starfleet_library").alias("sf"))).actionGet();
            tc.admin().indices().aliases(new IndicesAliasesRequest().addAliasAction(AliasActions.add().indices("klingonempire","vulcangov").alias("nonsf"))).actionGet();
            tc.admin().indices().aliases(new IndicesAliasesRequest().addAliasAction(AliasActions.add().indices("public").alias("unrestricted"))).actionGet();
        }
        
        RestHelper rh = nonSslRestHelper();
        
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("").getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("", encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("", encodeBasicHeader("nagilum", "nagilum")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeDeleteRequest("nonexistentindex*", encodeBasicHeader("nagilum", "nagilum")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest(".nonexistentindex*", encodeBasicHeader("nagilum", "nagilum")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executePutRequest("searchguard/config/2", "{}",encodeBasicHeader("nagilum", "nagilum")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_NOT_FOUND, rh.executeGetRequest("searchguard/config/0", encodeBasicHeader("nagilum", "nagilum")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_NOT_FOUND, rh.executeGetRequest("xxxxyyyy/config/0", encodeBasicHeader("nagilum", "nagilum")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("", encodeBasicHeader("abc", "abc:abc")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", encodeBasicHeader("userwithnopassword", "")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", encodeBasicHeader("userwithblankpassword", "")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", encodeBasicHeader("worf", "wrongpasswd")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", new BasicHeader("Authorization", "Basic "+"wrongheader")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", new BasicHeader("Authorization", "Basic ")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", new BasicHeader("Authorization", "Basic")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", new BasicHeader("Authorization", "")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("", encodeBasicHeader("picard", "picard")).getStatusCode());

        for(int i=0; i< 10; i++) {
            Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, rh.executeGetRequest("", encodeBasicHeader("worf", "wrongpasswd")).getStatusCode());
        }
        
        Assert.assertEquals(HttpStatus.SC_OK, rh.executePutRequest("/theindex","{}",encodeBasicHeader("theindexadmin", "theindexadmin")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_CREATED, rh.executePutRequest("/theindex/type/1?refresh=true","{\"a\":0}",encodeBasicHeader("theindexadmin", "theindexadmin")).getStatusCode());
        //Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("/theindex/_analyze?text=this+is+a+test",encodeBasicHeader("theindexadmin", "theindexadmin")).getStatusCode());
        //Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("_analyze?text=this+is+a+test",encodeBasicHeader("theindexadmin", "theindexadmin")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeDeleteRequest("/theindex",encodeBasicHeader("theindexadmin", "theindexadmin")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeDeleteRequest("/klingonempire",encodeBasicHeader("theindexadmin", "theindexadmin")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("starfleet/_search", encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("_search", encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("starfleet/ships/_search?pretty", encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeDeleteRequest("searchguard/", encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executePostRequest("/searchguard/_close", null,encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executePostRequest("/searchguard/_upgrade", null,encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executePutRequest("/searchguard/_mapping/config","{}",encodeBasicHeader("worf", "worf")).getStatusCode());

        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("searchguard/", encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executePutRequest("searchguard/config/2", "{}",encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("searchguard/config/0",encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeDeleteRequest("searchguard/config/0",encodeBasicHeader("worf", "worf")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executePutRequest("searchguard/config/0","{}",encodeBasicHeader("worf", "worf")).getStatusCode());
        
        HttpResponse resc = rh.executeGetRequest("_cat/indices/public",encodeBasicHeader("bug108", "nagilum"));
        System.out.println(resc.getBody());
        //Assert.assertTrue(resc.getBody().contains("green"));
        Assert.assertEquals(HttpStatus.SC_OK, resc.getStatusCode());
        
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("role01_role02/type01/_search?pretty",encodeBasicHeader("user_role01_role02_role03", "user_role01_role02_role03")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("role01_role02/type01/_search?pretty",encodeBasicHeader("user_role01", "user_role01")).getStatusCode());

        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("spock/type01/_search?pretty",encodeBasicHeader("spock", "spock")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, rh.executeGetRequest("spock/type01/_search?pretty",encodeBasicHeader("kirk", "kirk")).getStatusCode());
        Assert.assertEquals(HttpStatus.SC_OK, rh.executeGetRequest("kirk/type01/_search?pretty",encodeBasicHeader("kirk", "kirk")).getStatusCode());
        
        System.out.println("ok");
//all
        
        
    }
    
    @Test
    public void testHTTPTrace() throws Exception {
        
       new org.elasticsearch.action.support.replication.ReplicationTask.Status("a").toString(); 
        
       setup(Settings.EMPTY, new DynamicSgConfig(), Settings.EMPTY, true, ClusterConfiguration.DEFAULT);

        try (TransportClient tc = getInternalTransportClient(this.clusterInfo, Settings.EMPTY)) {
            
            tc.admin().indices().create(new CreateIndexRequest("copysf")).actionGet();
            
            for(int i=0; i<50;i++) {
                tc.index(new IndexRequest("a").type("b").id(i+"").setRefreshPolicy(RefreshPolicy.IMMEDIATE).source("{\"content\":"+i+"}", XContentType.JSON)).actionGet();
            }
        }
        
        RestHelper rh = nonSslRestHelper();
        
        System.out.println("############ _bulk");
        String bulkBody = 
                "{ \"index\" : { \"_index\" : \"test\", \"_type\" : \"type1\", \"_id\" : \"1\" } }"+System.lineSeparator()+
                "{ \"field1\" : \"value1\" }" +System.lineSeparator()+
                "{ \"index\" : { \"_index\" : \"test\", \"_type\" : \"type1\", \"_id\" : \"2\" } }"+System.lineSeparator()+
                "{ \"field2\" : \"value2\" }"+System.lineSeparator()+
                "{ \"delete\" : { \"_index\" : \"test\", \"_type\" : \"type1\", \"_id\" : \"2\" } }"+System.lineSeparator();
        
        rh.executePostRequest("_bulk?refresh=true", bulkBody, encodeBasicHeader("nagilum", "nagilum"));
        
        System.out.println("############ _bulk");
        bulkBody = 
                "{ \"index\" : { \"_index\" : \"test\", \"_type\" : \"type1\", \"_id\" : \"1\" } }"+System.lineSeparator()+
                "{ \"field1\" : \"value1\" }" +System.lineSeparator()+
                "{ \"index\" : { \"_index\" : \"test\", \"_type\" : \"type1\", \"_id\" : \"2\" } }"+System.lineSeparator()+
                "{ \"field2\" : \"value2\" }"+System.lineSeparator()+
                "{ \"delete\" : { \"_index\" : \"test\", \"_type\" : \"type1\", \"_id\" : \"2\" } }"+System.lineSeparator();
        
        rh.executePostRequest("_bulk?refresh=true", bulkBody, encodeBasicHeader("nagilum", "nagilum"));
       
        
        System.out.println("############ cat indices");
        //cluster:monitor/state
        //cluster:monitor/health
        //indices:monitor/stats
        rh.executeGetRequest("_cat/indices", encodeBasicHeader("nagilum", "nagilum"));

        
        System.out.println("############ _search");
        //indices:data/read/search
        rh.executeGetRequest("_search?refresh=true", encodeBasicHeader("nagilum", "nagilum"));

        System.out.println("############ get");
        //indices:data/read/get
        rh.executeGetRequest("a/b/1?refresh=true", encodeBasicHeader("nagilum", "nagilum"));

        System.out.println("############ index (+create index)");
        //indices:data/write/index
        //indices:data/write/bulk
        //indices:admin/create
        //indices:data/write/bulk[s]
        rh.executePostRequest("u/b/1?refresh=true", "{}",encodeBasicHeader("nagilum", "nagilum"));

        System.out.println("############ index only");
        //indices:data/write/index
        //indices:data/write/bulk
        //indices:admin/create
        //indices:data/write/bulk[s]
        rh.executePostRequest("u/b/2?refresh=true", "{}",encodeBasicHeader("nagilum", "nagilum"));
        
        System.out.println("############ update");
        //indices:data/write/index
        //indices:data/write/bulk
        //indices:admin/create
        //indices:data/write/bulk[s]
        System.out.println(rh.executePostRequest("u/b/2/_update?refresh=true", "{\"doc\" : {\"a\":1}}",encodeBasicHeader("nagilum", "nagilum")));
        
        System.out.println("############ delete");
        //indices:data/write/index
        //indices:data/write/bulk
        //indices:admin/create
        //indices:data/write/bulk[s]
        rh.executeDeleteRequest("u/b/2?refresh=true",encodeBasicHeader("nagilum", "nagilum"));
       
        System.out.println("############ msearch");
        String msearchBody = 
                "{\"index\":\"a\", \"type\":\"b\", \"ignore_unavailable\": true}"+System.lineSeparator()+
                "{\"size\":10, \"query\":{\"bool\":{\"must\":{\"match_all\":{}}}}}"+System.lineSeparator()+
                "{\"index\":\"a\", \"type\":\"b\", \"ignore_unavailable\": true}"+System.lineSeparator()+
                "{\"size\":10, \"query\":{\"bool\":{\"must\":{\"match_all\":{}}}}}"+System.lineSeparator()+
                "{\"index\":\"public\", \"ignore_unavailable\": true}"+System.lineSeparator()+
                "{\"size\":10, \"query\":{\"bool\":{\"must\":{\"match_all\":{}}}}}"+System.lineSeparator();
                         
            
        System.out.println(rh.executePostRequest("_msearch", msearchBody, encodeBasicHeader("nagilum", "nagilum")));
     
        System.out.println("############ mget");
        String mgetBody = "{"+
                "\"docs\" : ["+
                    "{"+
                         "\"_index\" : \"a\","+
                        "\"_type\" : \"b\","+
                        "\"_id\" : \"11\""+
                   " },"+
                   " {"+
                       "\"_index\" : \"a\","+
                       " \"_type\" : \"b\","+
                       " \"_id\" : \"12\""+
                    "}"+
                "]"+
            "}";
        
        System.out.println(rh.executePostRequest("_mget?refresh=true", mgetBody, encodeBasicHeader("nagilum", "nagilum")));
        
        System.out.println("############ delete by query");
        String dbqBody = "{"+
        ""+
        "  \"query\": { "+
        "    \"match\": {"+
        "      \"content\": 12"+
        "    }"+
        "  }"+
        "}";
        
        System.out.println(rh.executePostRequest("a/b/_delete_by_query", dbqBody, encodeBasicHeader("nagilum", "nagilum")));
        
        Thread.sleep(50000);
    }

}
