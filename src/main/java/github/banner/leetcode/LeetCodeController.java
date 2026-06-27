package github.banner.leetcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeetCodeController {

    @Autowired
    private LeetCodeService Service;

    @GetMapping(value = "/api/streak/{username}", produces = "image/svg+xml")
    public ResponseEntity<String> getStreakSvg(@PathVariable String username) {
        LeetCodeStats stats = Service.getUserStats(username);
        String svgContent = "<svg xmlns='http://www.w3.org/2000/svg' width='450' height='130' viewBox='0 0 450 130'>" +
                "  " +
                "  <rect width='100%' height='100%' rx='12' fill='#1e1e1e' stroke='#ffa116' stroke-width='1.5'/>" +

                "  " +
                "  <text x='20' y='30' fill='#eff2f6' font-family='Segoe UI, sans-serif' font-size='15' font-weight='600' opacity='0.8'>" + username + "'s LeetCode Status</text>" +
                "  <line x1='20' y1='42' x2='430' y2='42' stroke='#333333' stroke-width='1'/>" +

                "  " +
                "  <text x='25' y='70' fill='#ffa116' font-family='Segoe UI, sans-serif' font-size='26' font-weight='bold'>" + stats.getStreak() + "</text>" +
                "  <text x='25' y='90' fill='#8a8a8a' font-family='Segoe UI, sans-serif' font-size='12'>Day Streak</text>" +

                "  " +
                "  <text x='135' y='70' fill='#ffffff' font-family='Segoe UI, sans-serif' font-size='26' font-weight='bold'>" + stats.getTotalSolved() + "</text>" +
                "  <text x='135' y='90' fill='#8a8a8a' font-family='Segoe UI, sans-serif' font-size='12'>Total Solved</text>" +

                "  " +
                "  <line x1='235' y1='55' x2='235' y2='110' stroke='#333333' stroke-width='1.5'/>" +

                "  " +
                "  " +
                "  <text x='260' y='68' fill='#00b8a3' font-family='Segoe UI, sans-serif' font-size='13' font-weight='600'>Easy</text>" +
                "  <text x='340' y='68' fill='#ffffff' font-family='Segoe UI, sans-serif' font-size='14' font-weight='bold' text-anchor='end'>" + stats.getEasySolved() + "</text>" +

                "  " +
                "  <text x='260' y='90' fill='#ffc01e' font-family='Segoe UI, sans-serif' font-size='13' font-weight='600'>Medium</text>" +
                "  <text x='340' y='90' fill='#ffffff' font-family='Segoe UI, sans-serif' font-size='14' font-weight='bold' text-anchor='end'>" + stats.getMediumSolved() + "</text>" +

                "  " +
                "  <text x='260' y='112' fill='#ff2d55' font-family='Segoe UI, sans-serif' font-size='13' font-weight='600'>Hard</text>" +
                "  <text x='340' y='112' fill='#ffffff' font-family='Segoe UI, sans-serif' font-size='14' font-weight='bold' text-anchor='end'>" + stats.getHardSolved() + "</text>" +
                "</svg>";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl(CacheControl.noCache().mustRevalidate());
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(svgContent);
    }
}
