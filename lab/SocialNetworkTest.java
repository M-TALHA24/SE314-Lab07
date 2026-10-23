/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashSet;
import org.junit.Test;
import java.time.Instant;
import java.util.Arrays;

public class SocialNetworkTest {
    
// Task 02: Test 6 - Empty graph
@Test
public void task02EmptyGraph() {

    Map<String, Set<String>> graph = new HashMap<>();

    List<String> result = SocialNetwork.influencers(graph);

    assertTrue(result.isEmpty());
}

// Task 02: Test 7 - Single user without followers
@Test
public void task02NoFollowers() {

    Map<String, Set<String>> graph = new HashMap<>();

    graph.put("Alice", new HashSet<>());

    List<String> result = SocialNetwork.influencers(graph);

    assertTrue(result.isEmpty());
}

// Task 02: Test 8 - Single influencer
@Test
public void task02SingleInfluencer() {

    Map<String, Set<String>> graph = new HashMap<>();

    graph.put("Alice", new HashSet<>(
        Arrays.asList("Bob")
    ));

    List<String> result = SocialNetwork.influencers(graph);

    assertEquals(Arrays.asList("Bob"), result);
}

// Task 02: Test 9 - Multiple influencers
@Test
public void task02MultipleInfluencers() {

    Map<String, Set<String>> graph = new HashMap<>();

    graph.put("Alice", new HashSet<>(
        Arrays.asList("Bob", "Charlie")
    ));

    graph.put("David", new HashSet<>(
        Arrays.asList("Bob")
    ));

    List<String> result = SocialNetwork.influencers(graph);

    assertEquals(Arrays.asList("Bob", "Charlie"), result);
}

// Task 02: Test 10 - Tied influence
@Test
public void task02TiedInfluence() {

    Map<String, Set<String>> graph = new HashMap<>();

    graph.put("Alice", new HashSet<>(
        Arrays.asList("Bob")
    ));

    graph.put("David", new HashSet<>(
        Arrays.asList("Charlie")
    ));

    List<String> result = SocialNetwork.influencers(graph);

    assertEquals(
        new HashSet<>(Arrays.asList("Bob", "Charlie")),
        new HashSet<>(result)
    );
}

/*
 * Task 01 Testing Strategy:
 * Partition tweet input into empty lists, tweets
 * without mentions, single mentions, multiple
 * mentions, and repeated tweets by one author.
 *
 * Tests verify required following relationships
 * without assuming that no extra edges are allowed.
 */

private Tweet createTweet(long id, String author, String text) {
    return new Tweet(
        id, author, text,
        Instant.parse("2026-10-23T10:00:00Z")
    );
}

private boolean hasFollow(
        Map<String, Set<String>> graph,
        String author, String target) {

    for (String user : graph.keySet()) {
        if (user.equalsIgnoreCase(author)) {
            for (String followed : graph.get(user)) {
                if (followed.equalsIgnoreCase(target)) {
                    return true;
                }
            }
        }
    }

    return false;
}

@Test
public void task01NoMentions() {
    List<Tweet> tweets = Arrays.asList(
        createTweet(1, "Alice", "Hello everyone")
    );

    Map<String, Set<String>> graph =
        SocialNetwork.guessFollowsGraph(tweets);

    assertTrue(graph.isEmpty());
}

@Test
public void task01SingleMention() {
    List<Tweet> tweets = Arrays.asList(
        createTweet(2, "Ernie", "Hello @Bert")
    );

    Map<String, Set<String>> graph =
        SocialNetwork.guessFollowsGraph(tweets);

    assertTrue(hasFollow(graph, "Ernie", "Bert"));
}

@Test
public void task01MultipleMentions() {
    List<Tweet> tweets = Arrays.asList(
        createTweet(3, "Alice", "Hi @Bob and @Carol")
    );

    Map<String, Set<String>> graph =
        SocialNetwork.guessFollowsGraph(tweets);

    assertTrue(hasFollow(graph, "Alice", "Bob"));
    assertTrue(hasFollow(graph, "Alice", "Carol"));
}

@Test
public void task01MultipleTweets() {
    List<Tweet> tweets = Arrays.asList(
        createTweet(4, "Alice", "Hello @Bob"),
        createTweet(5, "ALICE", "Hi @Carol"),
        createTweet(6, "Alice", "Again @Bob")
    );

    Map<String, Set<String>> graph =
        SocialNetwork.guessFollowsGraph(tweets);

    assertTrue(hasFollow(graph, "Alice", "Bob"));
    assertTrue(hasFollow(graph, "Alice", "Carol"));
}


    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
