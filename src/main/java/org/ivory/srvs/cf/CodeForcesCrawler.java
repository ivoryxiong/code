package org.ivory.srvs.cf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ivory.srvs.cf.domain.model.Problem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CodeForcesCrawler {
	// http://codeforces.com/api/contest.list?gym=true

	private CodeForcesProblemParser parser = new CodeForcesProblemParser();
	
	public void fetchContestList() {
		try {

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet("http://codeforces.com/api/contest.list?gym=true");
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			JSONTokener tokener = new JSONTokener(new InputStreamReader((response.getEntity().getContent())));

			JSONObject jsonObj = new JSONObject(tokener);
			JSONArray contests = (JSONArray) jsonObj.get("result");
			for (int i = 0; i < contests.length(); i++) {
				JSONObject contest = contests.getJSONObject(i);
				System.out.println(contest);
				;
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// http://codeforces.com/api/problemset.problems
	//
	public void fetchProblems() {
		List<Problem> ps = new ArrayList<Problem>();
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet("http://codeforces.com/api/problemset.problems");
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			JSONTokener tokener = new JSONTokener(new InputStreamReader((response.getEntity().getContent())));

			JSONObject jsonObj = new JSONObject(tokener);
			JSONArray problems = (JSONArray) jsonObj.getJSONObject("result").get("problems");
			for (int i = 0; i < problems.length(); i++) {
				JSONObject problem = problems.getJSONObject(i);
				Problem p = new Problem();
				p.name = problem.getString("name");
				p.contestId = "" + problem.getInt("contestId");
				p.pid = "cf-" + p.contestId + "-" + problem.getString("index");
				p.url = "http://codeforces.com/problemset/problem/" + p.contestId + "/" + problem.getString("index");
				JSONArray ts = problem.getJSONArray("tags");
				List<String> tags = new ArrayList<String>();
				for(int j= 0; j < ts.length(); j ++) {
					tags.add(ts.getString(j));
				}
				p.tags = tags;
				ps.add(p);
				System.out.println(p);
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		int idx = 0 ;
		for (Problem p : ps) {
			idx ++;
			if (idx % 50 == 0) {
				System.out.println(p);
				String html = fetchProblemDetail(p);
				parser.parseProblem(html, p);
			}
		}
	}
	
	public String fetchProblemDetail(Problem problem) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(problem.url);
			getRequest.addHeader("accept", "text/html;charset=UTF-8");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String line = null;
			StringBuilder html = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				html.append(line);
			}
			return html.toString();
		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}		
		
		return null;
	}
}
