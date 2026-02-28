package com.clinicsystem.clinicapi.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to inline CSS styles for email compatibility.
 * Email clients (Gmail, Outlook, Yahoo) strip <head> and <style> tags,
 * so we need to convert CSS rules to inline styles.
 */
public class EmailCssInliner {

    private static final Pattern CSS_VARIABLE_PATTERN = Pattern.compile("var\\(--([^)]+)\\)");

    /**
     * Inline CSS styles from <style> tags into HTML elements.
     * Also replaces CSS variables with their actual values.
     * 
     * @param html Original HTML with <style> tags
     * @return HTML with inlined styles and CSS variables replaced
     */
    public static String inlineCSS(String html) {
        Document doc = Jsoup.parse(html);

        // Extract CSS variables from :root
        Map<String, String> cssVariables = extractCssVariables(doc);

        // Extract all style tags
        Elements styleTags = doc.select("style");
        StringBuilder allCss = new StringBuilder();

        for (Element styleTag : styleTags) {
            allCss.append(styleTag.html()).append("\n");
        }

        // Parse CSS and apply inline styles
        String cssContent = allCss.toString();
        Map<String, Map<String, String>> cssRules = parseCssRules(cssContent);

        // Apply CSS rules to matching elements
        for (Map.Entry<String, Map<String, String>> entry : cssRules.entrySet()) {
            String selector = entry.getKey();
            Map<String, String> styles = entry.getValue();

            // Skip :root, *, ::before, ::after selectors
            if (selector.equals(":root") || selector.equals("*") ||
                    selector.contains("::before") || selector.contains("::after")) {
                continue;
            }

            try {
                Elements elements = doc.select(selector);
                for (Element element : elements) {
                    String currentStyle = element.attr("style");
                    String newStyle = buildInlineStyle(styles, cssVariables);

                    if (!currentStyle.isEmpty()) {
                        element.attr("style", currentStyle + "; " + newStyle);
                    } else {
                        element.attr("style", newStyle);
                    }
                }
            } catch (Exception e) {
                // Skip invalid selectors
            }
        }

        // Remove style tags and external links
        doc.select("style").remove();
        doc.select("link[rel=stylesheet]").remove();
        doc.select("link[rel=preconnect]").remove();

        return doc.html();
    }

    /**
     * Extract CSS variables from :root selector
     */
    private static Map<String, String> extractCssVariables(Document doc) {
        Map<String, String> variables = new HashMap<>();
        Elements styleTags = doc.select("style");

        for (Element styleTag : styleTags) {
            String css = styleTag.html();
            Pattern rootPattern = Pattern.compile(":root\\s*\\{([^}]+)\\}", Pattern.DOTALL);
            Matcher matcher = rootPattern.matcher(css);

            if (matcher.find()) {
                String rootContent = matcher.group(1);
                Pattern varPattern = Pattern.compile("--([\\w-]+)\\s*:\\s*([^;]+);");
                Matcher varMatcher = varPattern.matcher(rootContent);

                while (varMatcher.find()) {
                    String varName = varMatcher.group(1).trim();
                    String varValue = varMatcher.group(2).trim();
                    variables.put(varName, varValue);
                }
            }
        }

        return variables;
    }

    /**
     * Parse CSS rules into a map of selector -> properties
     */
    private static Map<String, Map<String, String>> parseCssRules(String css) {
        Map<String, Map<String, String>> rules = new HashMap<>();

        // Remove comments
        css = css.replaceAll("/\\*.*?\\*/", "");

        // Match CSS rules: selector { property: value; }
        Pattern rulePattern = Pattern.compile("([^{]+)\\{([^}]+)\\}", Pattern.DOTALL);
        Matcher matcher = rulePattern.matcher(css);

        while (matcher.find()) {
            String selector = matcher.group(1).trim();
            String properties = matcher.group(2).trim();

            // Parse properties
            Map<String, String> propMap = new HashMap<>();
            String[] props = properties.split(";");

            for (String prop : props) {
                if (prop.contains(":")) {
                    String[] parts = prop.split(":", 2);
                    if (parts.length == 2) {
                        String propName = parts[0].trim();
                        String propValue = parts[1].trim();
                        propMap.put(propName, propValue);
                    }
                }
            }

            if (!propMap.isEmpty()) {
                rules.put(selector, propMap);
            }
        }

        return rules;
    }

    /**
     * Build inline style string from CSS properties, replacing CSS variables
     */
    private static String buildInlineStyle(Map<String, String> styles, Map<String, String> cssVariables) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : styles.entrySet()) {
            String property = entry.getKey();
            String value = entry.getValue();

            // Replace CSS variables
            value = replaceCssVariables(value, cssVariables);

            sb.append(property).append(": ").append(value).append("; ");
        }

        return sb.toString().trim();
    }

    /**
     * Replace CSS variables like var(--navy) with actual values
     */
    private static String replaceCssVariables(String value, Map<String, String> variables) {
        Matcher matcher = CSS_VARIABLE_PATTERN.matcher(value);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            String varValue = variables.getOrDefault(varName, matcher.group(0));
            matcher.appendReplacement(result, Matcher.quoteReplacement(varValue));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
