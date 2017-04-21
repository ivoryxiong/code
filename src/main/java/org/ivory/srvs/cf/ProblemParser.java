package org.ivory.srvs.cf;

import org.ivory.srvs.cf.domain.model.Problem;

public abstract class ProblemParser {	
	public abstract void parseProblem(String html, Problem problem);
}
