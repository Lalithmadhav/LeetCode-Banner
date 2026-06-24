package github.banner.leetcode;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class LeetCodeService {

    private final String LEETCODE_GRAPHQL_URL = "https://leetcode.com/graphql";

    // Using a clean, standard Webkit User-Agent string
    private final String BROWSER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";

    public LeetCodeStats getUserStats(String username) {
        RestTemplate restTemplate = new RestTemplate();

        String streak = "0", easy = "0", medium = "0", hard = "0", total = "0";

        try {
            // STEP 1: Define the query directly
            String graphQLQuery = "query userProfileData($username: String!) { " +
                    "  matchedUser(username: $username) { " +
                    "    userCalendar { streak } " +
                    "    submitStats: submitStatsGlobal { " +
                    "      acSubmissionNum { difficulty count } " +
                    "    } " +
                    "  } " +
                    "}";

            Map<String, Object> variables = new HashMap<>();
            variables.put("username", username);
            GraphQLRequest payload = new GraphQLRequest(graphQLQuery, variables);

            // STEP 2: Configure request headers to masquerade safely
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("User-Agent", BROWSER_USER_AGENT);
            headers.add("Referer", "https://leetcode.com/" + username);
            headers.add("Origin", "https://leetcode.com");

            HttpEntity<GraphQLRequest> entity = new HttpEntity<>(payload, headers);

            // STEP 3: Post directly to /graphql
            ResponseEntity<Map> response = restTemplate.postForEntity(LEETCODE_GRAPHQL_URL, entity, Map.class);

            Map body = response.getBody();
            if (body == null || !body.containsKey("data")) {
                System.out.println("❌ ERROR: Empty data wrapper returned.");
                return new LeetCodeStats("0", "0", "0", "0", "0");
            }

            Map data = (Map) body.get("data");
            Map matchedUser = (Map) data.get("matchedUser");

            if (matchedUser == null) {
                System.out.println("❌ ERROR: Username '" + username + "' not found. (Check case sensitivity!)");
                return new LeetCodeStats("0", "0", "0", "0", "0");
            }

            // Parse Streak Safely
            Map userCalendar = (Map) matchedUser.get("userCalendar");
            if (userCalendar != null && userCalendar.get("streak") != null) {
                streak = userCalendar.get("streak").toString();
            }

            // Parse Solved Stats Safely
            Map submitStats = (Map) matchedUser.get("submitStats");
            if (submitStats != null && submitStats.containsKey("acSubmissionNum")) {
                List<Map> acSubmissions = (List<Map>) submitStats.get("acSubmissionNum");
                for (Map sub : acSubmissions) {
                    String diff = sub.get("difficulty").toString();
                    String count = sub.get("count").toString();
                    switch (diff) {
                        case "All" -> total = count;
                        case "Easy" -> easy = count;
                        case "Medium" -> medium = count;
                        case "Hard" -> hard = count;
                    }
                }
                System.out.println("✅ SUCCESS: Found data for " + username);
            }

        } catch (Exception e) {
            System.out.println("❌ EXCEPTION DURING API CALL:");
            e.printStackTrace();
        }

        return new LeetCodeStats(streak, easy, medium, hard, total);
    }
}

record GraphQLRequest(String query, Map<String, Object> variables) {}