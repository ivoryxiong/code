package org.ivory.srvs.cf.domain.model;

import java.util.List;

public class Problem {
	public String pid;
	public String contestId;
	public String name;
	public String url;
	public List<String> tags;
	
	@Override
	public String toString() {
		return "Problem [pid=" + pid + ", contestId=" + contestId + ", name=" + name + ", url=" + url + ", tags=" + tags
				+ "]";
	}

}
