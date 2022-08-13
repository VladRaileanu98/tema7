//tema7
// Raileanu Vlad-Alexandru
import com.google.gson.Gson;
import futures.Comment;
import futures.Post;
import futures.User;
import org.apache.catalina.realm.CombinedRealm;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainFuture{

    public static void gson() throws URISyntaxException, ExecutionException, InterruptedException, IOException {
        //task 1
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        System.out.println("\ntask#1");
        URI uriPosts= new URI("https://jsonplaceholder.typicode.com/posts");
        HttpRequest httpRequestPosts = HttpRequest.newBuilder(uriPosts).build();
        httpClient.sendAsync(httpRequestPosts, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response->{
                    List<Post> postList = Arrays.asList(gson.fromJson(response.body(), Post[].class));
                    List<String> list = postList.stream()
                            .filter(val->(val.getUserId()==3))
                            .map(postListExample->postListExample.getAllPostAttributesInStringForm()).collect(Collectors.toList());
                    System.out.println(list);
                }).get();
        //task 2
        System.out.println("\ntask#2:");
        URI uriComments= new URI("https://jsonplaceholder.typicode.com/comments");
        HttpRequest httpRequestComments = HttpRequest.newBuilder(uriComments).build();
        httpClient.sendAsync(httpRequestComments, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response->{
                    List<Comment> commentsList = Arrays.asList(gson.fromJson(response.body(), Comment[].class));
                    List<String> list = commentsList.stream()
                            .filter(val->(val.getPostId()==4))
                            .map(commentsSimpleList->commentsSimpleList.getEmail()).collect(Collectors.toList());
                    System.out.println(list);
                }).get();

        //task 3
        URI uriPostsDelete= new URI("https://jsonplaceholder.typicode.com/posts?userId=3");
        HttpClient httpClientDelete = HttpClient.newHttpClient();
        HttpRequest httpRequestDelete = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(uriPostsDelete)
                .build();
        HttpResponse<String> response = httpClientDelete.send(httpRequestDelete, HttpResponse.BodyHandlers.ofString());
        System.out.println("\ntask#3:\n"+response.body());


    }

    //task 4
    public static void thenCombinePostUserComment()throws URISyntaxException, ExecutionException, InterruptedException{
        //first CompletableFuture (made for "post title")
        Gson gson = new Gson();
        HttpClient httpClient = HttpClient.newHttpClient();

        URI uriPost4 = new URI("https://jsonplaceholder.typicode.com/posts/4");
        HttpRequest httpRequestPost = HttpRequest.newBuilder(uriPost4).build();
        CompletableFuture<String> completableFuturePostTitle =httpClient
                .sendAsync(httpRequestPost, HttpResponse.BodyHandlers.ofString())
                .thenApply(response->{
                    Post post = gson.fromJson(response.body(), Post.class);
                    return post.getTitle();
                });


        //second CompletableFuture (made for "full name of user who posted it")
        URI uriUsersPost = new URI("https://jsonplaceholder.typicode.com/users/4");
        HttpRequest httpRequestGetUsersNameForPost4 = HttpRequest.newBuilder(uriUsersPost).build();
        CompletableFuture<String> completableFutureUsersNameForPost4 =httpClient
                .sendAsync(httpRequestGetUsersNameForPost4, HttpResponse.BodyHandlers.ofString())
                .thenApply(response-> {
                    User wantedUser = gson.fromJson(response.body(), User.class);
                    return wantedUser.getName();
                });

        URI uriCommentsPost = new URI("https://jsonplaceholder.typicode.com/comments");
        HttpRequest httpRequestGetCommentsForPost4 = HttpRequest.newBuilder(uriCommentsPost).build();
        CompletableFuture<String> completableFutureCommentsForPost4 = httpClient
                .sendAsync(httpRequestGetCommentsForPost4, HttpResponse.BodyHandlers.ofString())
                        .thenApply(response-> {
                            List<Comment> listComment = Arrays.asList(gson.fromJson(response.body(), Comment[].class));
                            List<String> commentNameListForPost4 = listComment.stream()
                                    .filter(commentExample->commentExample.getPostId()==4)
                                    .map(commentsName->commentsName.getName()).collect(Collectors.toList());
                            String formattedList = Arrays.toString(commentNameListForPost4.toArray())
                                    .replace("["," ")
                                    .replace("]", "\n")
                                    .replace(",", "\n");
                           return formattedList;
                        });
        System.out.println("\ntask#4:");
        //print first&second completable future
        completableFuturePostTitle.thenCombine(completableFutureUsersNameForPost4,(title,name)->{
            return " " + title+"\n "+name;
        })
                .thenAccept(System.out::println)
                .get();

        //print third completable future
        completableFuturePostTitle.thenCombine(completableFutureCommentsForPost4,(title,comments)->{
                    return comments;
                })
                .thenAccept(System.out::println)
                .get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        gson();
        thenCombinePostUserComment();
    }
}
