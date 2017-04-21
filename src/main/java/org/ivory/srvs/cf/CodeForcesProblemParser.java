package org.ivory.srvs.cf;

import org.ivory.srvs.cf.domain.model.Problem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CodeForcesProblemParser extends ProblemParser {

	@Override
	public void parseProblem(String html, Problem problem) {
		System.out.println(html);
		Document doc = Jsoup.parse(html);
		Element ttypography = doc.select("div.ttypography").first();
		System.out.println(ttypography.text());
	}

}
