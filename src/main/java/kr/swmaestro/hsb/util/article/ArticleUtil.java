package kr.swmaestro.hsb.util.article;

import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.UserInfo;

public class ArticleUtil {
	public static Article setArticleInfoWithSession(Article article,UserInfo userInfo){
		article.setWriterId(userInfo.getId());
		article.setWriterNickname(userInfo.getNickname());
		article.setWriterUsername(userInfo.getUsername());
		return article;
	}
}
