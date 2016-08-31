package com.shenjianli.fly;

import org.junit.Test;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_mockServer()throws Exception{
        // 创建一个 MockWebServer
        MockWebServer server = new MockWebServer();

        // 设置响应
        server.enqueue(new MockResponse().setBody("hello, world!"));
        server.enqueue(new MockResponse().setBody("sup, bra?"));
        server.enqueue(new MockResponse().setBody("yo dog"));

        // 启动服务
        // Start the server.
        server.start();

        // 设置服务端的URL，客户端请求中使用
        HttpUrl baseUrl = server.url("/v1/chat");

        // 运行你的应用程序代码，进行HTTP请求
        // 响应会按照上面设置中放入队列的顺序被返回
 //       Chat chat = new Chat(baseUrl);
//
//        chat.loadMore();
//        assertEquals("hello, world!", chat.message());
//
//        chat.loadMore();
//        chat.loadMore();
//        assertEquals(""
//                + "hello, world!\n"
//                + "sup, bra?\n"
//                + "yo dog", chat.message());

        // 可选：确认你的应用做出了正确的请求
//        RecordedRequest request1 = server.takeRequst();
//        assertEquals("/v1/chat/messages/", request1.getPath());
//        assertNotNull(request1.getHeader("Authorization"));
//
//        RecordedRequest request2 = server.takeRequest();
//        assertEquals("/v1/chat/message/2", request.getPath());
//
//        RecordedRequest request3 = server.takeRequest();
//        assertEquals("/v1/chat/message/3", request.getPath());

        // 关闭服务，因为不能重用
        server.shutdown();
    }
}