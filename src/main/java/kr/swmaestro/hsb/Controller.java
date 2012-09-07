package kr.swmaestro.hsb;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import kr.swmaestro.hsb.auth.AuthManager;
import kr.swmaestro.hsb.auth.AuthUserInfo;
import kr.swmaestro.hsb.data.JsonIgnoreResultModelPropertyesMixIn;
import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.Comment;
import kr.swmaestro.hsb.model.ErrorInfo;
import kr.swmaestro.hsb.model.Follow;
import kr.swmaestro.hsb.model.Result;
import kr.swmaestro.hsb.model.SecureKeyModel;
import kr.swmaestro.hsb.model.UserInfo;
import kr.swmaestro.hsb.service.ArticleService;
import kr.swmaestro.hsb.service.CommentService;
import kr.swmaestro.hsb.service.FollowService;
import kr.swmaestro.hsb.service.UserService;
import kr.swmaestro.hsb.util.PasswordEncoder;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
@RequestMapping()
public class Controller {
	
	@Autowired
	private AuthManager authManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private FollowService followerService;
	
	@Autowired
	private CommentService commentService;
	
	// 오류 체크
	private boolean errorCheck(Result result, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Set<ErrorInfo> errors = new HashSet<>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				ErrorInfo error = new ErrorInfo();
				if (objectError instanceof FieldError) {
					error.setCode(objectError.getCode() + "." + objectError.getObjectName() + "." + ((FieldError) objectError).getField());
				} else {
					error.setCode(objectError.getCode() + "." + objectError.getObjectName());
				}
				error.setDefaultMessage(objectError.getDefaultMessage());
				Set<Object> arguments = new HashSet<>();
				if (objectError.getArguments() != null) {
					for (int i = 1 ; i < objectError.getArguments().length ; i++) {
						arguments.add(objectError.getArguments()[i]);
					}
				}
				error.setArguments(arguments);
				errors.add(error);
			}
			result.setErrors(errors);
			result.setSuccess(false);
			return false;
		}
		return true;
	}
	
	private ResponseEntity<String> resultToJson(Result result) {

		String errors = null;
		if (result.getErrors() != null) {
			ObjectMapper om = new ObjectMapper();
			try {
				// 필요없는 property 제외
				om.getSerializationConfig().addMixInAnnotations(ErrorInfo.class, JsonIgnoreResultModelPropertyesMixIn.class);
				errors = om.writeValueAsString(result.getErrors());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String ret = "";
		if (result.getData() != null) {
			ret += "{";
			if (!result.isSuccess() ||  errors != null) {
				ret += "\"success\":false,";
				ret += "\"errors\":" + errors + ",";
			} else {
				ret += "\"success\":true,";
			}
			ret += "\"single\":true,";
			ret += "\"data\":";
			ret += result.getData();
			ret += "}";
		} else if (result.getList() != null) {
			ret += "{";
			if (!result.isSuccess() ||  errors != null) {
				ret += "\"success\":false,";
				ret += "\"errors\":" + errors + ",";
			} else {
				ret += "\"success\":true,";
			}
			ret += "\"single\":false,";
			ret += "\"list\":[";
			for (int i = 0 ; i < result.getList().size() ; i++) {
				ret += result.getList().get(i);
				if (i != result.getList().size() - 1) {
					ret += ',';
				}
			}
			ret += "]}";
		} else { // 오류
			ret += "{";
			if (!result.isSuccess() ||  errors != null) {
				ret += "\"success\":false,";
				ret += "\"errors\":" + errors + ",";
			} else {
				ret += "\"success\":true,";
			}
			ret += "\"single\":false";
			ret += "}";
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
		return new ResponseEntity<String>(ret, responseHeaders, HttpStatus.CREATED);
	}
	
	// 결과값 반환
	private ResponseEntity<String> returnJson(Result result, Model model) {
		return resultToJson(result);
	}
	
	// 결과값 반환
	private ResponseEntity<String> returnJson(Result result, SecureKeyModel data, Model model) {
		
		data.setSecureKey(null);
		
		String json = null;
		
		ObjectMapper om = new ObjectMapper();
		try {
			// 필요없는 property 제외
			om.getSerializationConfig().addMixInAnnotations(data.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
			
			json = om.writeValueAsString(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnJson(result, json, model);
	}
	
	// 결과값 반환
	private ResponseEntity<String> returnJson(Result result, String data, Model model) {
		result.setData(data);
		return resultToJson(result);
	}
	
	// 결과값 반환
	private ResponseEntity<String> returnJson(Result result, List<String> list, Model model) {
		result.setList(list);
		return resultToJson(result);
	}
	
	// 인증처리
	private Result authCheck(String secureKey, Model model) {
		if (authManager.isAuthenticated(secureKey)) {
			return null;
		} else {
			Result result = new Result();
			ErrorInfo error = new ErrorInfo();
			error.setCode("NeedAuth");
			error.setDefaultMessage("인증이 필요합니다.");
			Set<ErrorInfo> errors = new HashSet<>();
			errors.add(error);
			result.setSuccess(false);
			result.setErrors(errors);
			return result;
		}
	}
	
	// 인증처리
	private Result authCheck(SecureKeyModel secureKeyModel, Model model) {
		return authCheck(secureKeyModel.getSecureKey(), model);
	}
	
	@RequestMapping()
	public String main(Model model) {
		return "main";
	}
	
	@RequestMapping("admin/test")
	public void test(Model model) {}
	
	// 로그인
	@RequestMapping(value = "user/auth", method = RequestMethod.POST) // 인증 생성
	public ResponseEntity<String> login(@Valid AuthUserInfo authUserInfo, BindingResult bindingResult, Model model, HttpServletRequest request) {
		Result result = new Result();
		
		UserInfo userInfo = null;
		
		if (!UserInfo.existsUser(authUserInfo.getUsername())) {
			bindingResult.rejectValue("username", "NotExists.authUserInfo.username", "존재하지 않는 아이디입니다.");
		} else {
			userInfo = UserInfo.findUserInfoByUsername(authUserInfo.getUsername());
			if (!bindingResult.hasFieldErrors("password") && !userInfo.getPassword().equals(PasswordEncoder.encodePassword(authUserInfo.getPassword()))) {
				bindingResult.rejectValue("password", "Wrong.authUserInfo.password", "잘못된 비밀번호입니다.");
			}
		}
		if (errorCheck(result, bindingResult)) {
			String secureKey = authManager.auth(userInfo);
			
			userInfo.setLastLoginDate(new Date());
			userInfo.increaseLoginCount();
			userService.saveUserInfo(userInfo);
			
			authUserInfo.setGeneratedSecureKey(secureKey);
			
			// 성공~!
			result.setSuccess(true);
		}
		
		return returnJson(result, authUserInfo, model);
	}
	
	// 로그아웃
	// 인증 필요
	@RequestMapping(value = "user/auth", method = RequestMethod.DELETE) // 인증 제거
	public ResponseEntity<String> logout(String secureKey, Model model) {
		Result result = new Result();
		authManager.unauth(secureKey);
		result.setSuccess(true);
		return returnJson(result, model);
	}
	
	// 회원가입
	@RequestMapping(value = "user/account", method = RequestMethod.POST)
	public ResponseEntity<String> join(@Valid UserInfo userInfo, BindingResult bindingResult, Model model) {
		Result result = new Result();
		
		if (!bindingResult.hasFieldErrors("password") && !userInfo.getPassword().equals(userInfo.getPasswordConfirm())) {
			bindingResult.rejectValue("password", "Equals.userInfo.passwordConfirm", "비밀번호와 비밀번호 확인이 다릅니다.");
		}
		if (!bindingResult.hasFieldErrors("username") && UserInfo.realExistsUser(userInfo.getUsername())) {
			bindingResult.rejectValue("username", "Exists.userInfo.username", "이미 존재하는 아이디입니다.");
		}
		if (!bindingResult.hasFieldErrors("nickname") && UserInfo.realExistsNickname(userInfo.getNickname())) {
			bindingResult.rejectValue("nickname", "Exists.userInfo.nickname", "이미 존재하는 닉네임입니다.");
		}
		
		if (errorCheck(result, bindingResult)) {
			// 암호화
			userInfo.setPassword(PasswordEncoder.encodePassword(userInfo.getPassword()));
			userInfo.setJoinDate(new Date());
			
			userService.saveUserInfo(userInfo);
			
			// 성공~!
			result.setSuccess(true);
		}
		return returnJson(result, userInfo, model);
	}
	
	// 회원 정보 수정
	// 인증 필요
	@RequestMapping(value = "user/account", method = RequestMethod.PUT)
	public ResponseEntity<String> updateAccount(@Valid UserInfo userInfo, BindingResult bindingResult, Model model) {
		Result result = new Result();
		String secureKey = userInfo.getSecureKey();
		
		Result authResult = authCheck(userInfo, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
			
		UserInfo originUserInfo = UserInfo.findUserInfo(authManager.getUserId(secureKey));
		String password = PasswordEncoder.encodePassword(userInfo.getPassword());
		
		if (!bindingResult.hasFieldErrors("password") && !originUserInfo.getPassword().equals(password) && !userInfo.getPassword().equals(userInfo.getPasswordConfirm())) {
			bindingResult.rejectValue("password", "Equals.userInfo.passwordConfirm", "비밀번호와 비밀번호 확인이 다릅니다.");
		}
		if (!bindingResult.hasFieldErrors("username") && !originUserInfo.getUsername().equals(userInfo.getUsername()) && UserInfo.realExistsUser(userInfo.getUsername())) {
			bindingResult.rejectValue("username", "Exists.userInfo.username", "이미 존재하는 아이디입니다.");
		}
		if (!bindingResult.hasFieldErrors("nickname") && !originUserInfo.getNickname().equals(userInfo.getNickname()) && UserInfo.realExistsNickname(userInfo.getNickname())) {
			bindingResult.rejectValue("nickname", "Exists.userInfo.nickname", "이미 존재하는 닉네임입니다.");
		}
		
		if (errorCheck(result, bindingResult)) {

			originUserInfo.setPassword(password);
			originUserInfo.setUsername(userInfo.getUsername());
			originUserInfo.setNickname(userInfo.getNickname());
			
			userService.saveUserInfo(originUserInfo);
			
			// 로그인 정보에 재삽입
			authManager.setUserInfo(secureKey, originUserInfo);
			
			// 성공~!
			result.setSuccess(true);
		}
		
		return returnJson(result, userInfo, model);
	}
	
	// 회원 정보 삭제 (탈퇴)
	// 인증 필요
	@RequestMapping(value = "user/account", method = RequestMethod.DELETE)
	public ResponseEntity<String> leave(String secureKey, Model model) {
		Result result = new Result();
		
		Result authResult = authCheck(secureKey, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		UserInfo userInfo = UserInfo.findUserInfo(authManager.getUserId(secureKey));
		userService.deleteUserInfo(userInfo);
		
		result.setSuccess(true);
		
		return returnJson(result, model);
	}
	
	// 타임라인
	// 인증 필요
	@RequestMapping(value = "user/timeline", method = RequestMethod.GET)
	public ResponseEntity<String>  timeline(String secureKey, @RequestParam(defaultValue = "0") long beforeArticleId, @RequestParam(defaultValue = "10") int count, Model model) {
		Result result = new Result();
		
		if (count < 1 || count > 100) { // 최대 100개
			count = 10; // 기본 10개
		}
		
		Result authResult = authCheck(secureKey, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		UserInfo userInfo = UserInfo.findUserInfo(authManager.getUserId(secureKey));
		List<String> articleJsonList = articleService.timelineJsonByWriterId(userInfo.getId(), 0l, count);
		
		result.setSuccess(true);
		
		return returnJson(result, articleJsonList, model);
	}
	
	// 글 목록 보기
	@RequestMapping(value = "{username}", method = RequestMethod.GET)
	public ResponseEntity<String> home(@PathVariable String username, @RequestParam(defaultValue = "0") long beforeArticleId, @RequestParam(defaultValue = "10") int count, Model model) {
		Result result = new Result();
		
		if (count < 1 || count > 100) { // 최대 100개
			count = 10; // 기본 10개
		}
		
		UserInfo userInfo = UserInfo.findUserInfoByUsername(username);
		List<String> articleJsonList = articleService.findArticleJsonsByWriterId(userInfo.getId(), beforeArticleId, count);
		
		result.setSuccess(true);
		
		return returnJson(result, articleJsonList, model);
	}
	
	// 유저 정보 보기
	@RequestMapping(value = "{username}/info", method = RequestMethod.GET)
	public ResponseEntity<String> info(@PathVariable String username, Model model) {
		Result result = new Result();
		
		UserInfo userInfo = UserInfo.findUserInfoByUsername(username);
		
		result.setSuccess(true);
		
		return returnJson(result, userInfo, model);
	}
	
	// 글쓰기
	@RequestMapping(value = "{username}", method = RequestMethod.POST)
	public ResponseEntity<String> write(@PathVariable String username, @Valid Article article, BindingResult bindingResult, Model model) {
		Result result = new Result();
		
		Result authResult = authCheck(article, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		UserInfo userInfo = authManager.getUserInfo(article.getSecureKey());

		article.setWriterId(userInfo.getId());
		article.setWriterNickname(userInfo.getNickname());
		article.setWriterUsername(userInfo.getUsername());
		article.setEnable(true);
		
		// 유저네임이랑 로그인 유저가 같은지 판단하는 코드
		if (!bindingResult.hasFieldErrors("username") && !userInfo.getUsername().equals(username)) {
			bindingResult.rejectValue("username", "Equals.userInfo.username", "아이디가 다릅니다.");
		}
		
		if (errorCheck(result, bindingResult)) {
			article.setWriteDate(new Date());
			
			// 저장
			articleService.saveArticle(article);
			
			// 성공~!
			result.setSuccess(true);
		}
		return returnJson(result, article, model);
	}

	// 팔로우하기
	// 인증 필요
	@RequestMapping(value = "{username}/follow", method = RequestMethod.POST) // 팔로우 생성
	public ResponseEntity<String> follow(@PathVariable String username, @Valid Follow follower, BindingResult bindingResult, Model model) {
		Result result= new Result();
		
		Result authResult = authCheck(follower, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		UserInfo followingUser=authManager.getUserInfo(follower.getSecureKey());
		UserInfo followedUser = UserInfo.findUserInfoByUsername(username);
		
		//본인의 계정은 팔로우 할 수 없도록 validation
		if (followingUser.getId().equals(followedUser.getId()) ){
			bindingResult.rejectValue("id", "Equals.follower.userid", "본인의 아이디는 팔로우 할 수 없습니다.");
		}
		if(Follow.isFollowing(followedUser.getId(),followingUser.getId())){
			bindingResult.rejectValue("id", "Exists.follower.userid", "이미 팔로우한 아이디 입니다.");
		}
		if (errorCheck(result, bindingResult)) {
			follower.setTargetUserId(followedUser.getId());
			follower.setTargetUserNickname(followedUser.getNickname());
			follower.setTargetUserUsername(followedUser.getUsername());
			follower.setFollowerId(followingUser.getId());
			follower.setFollowerNickname(followingUser.getNickname());
			follower.setFollowerUsername(followingUser.getUsername());
			follower.setFollowDate(new Date());
		
			followerService.saveFollower(follower);
		
			// 성공
			result.setSuccess(true);
		}
		return returnJson(result,follower,model);
	}
	
	// 언팔로우
	// 인증 필요
	@RequestMapping(value = "{username}/follow", method = RequestMethod.DELETE) // 팔로우 제거
	public ResponseEntity<String> unfollow(@PathVariable String username,String secureKey,@Valid Follow follower,BindingResult bindingResult, Model model) {
		Result result= new Result();
		
		Result authResult = authCheck(secureKey, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		UserInfo followingUser=authManager.getUserInfo(follower.getSecureKey());
		UserInfo followedUser = UserInfo.findUserInfoByUsername(username);
		if(!Follow.isFollowing(followedUser.getId(),followingUser.getId())){
			bindingResult.rejectValue("id", "NonExists.follower.userid", "팔로우 관계가 아닙니다.");
		}
		if(errorCheck(result,bindingResult)){
			follower.setTargetUserId(followedUser.getId());
			follower.setFollowerId(followingUser.getId());
			followerService.removeFollow(follower);
			
			// 성공
			result.setSuccess(true);
		}
		return returnJson(result,follower,model);
	}
	
	// 팔로잉 목록
	@RequestMapping(value = "{username}/following", method = RequestMethod.GET)
	public ResponseEntity<String> following(@PathVariable String username, Model model) {
		Result result= new Result();
		
		UserInfo userInfo = UserInfo.findUserInfoByUsername(username);
		List<String> userJsonList=followerService.getFollowingJsonListByUserInfo(userInfo);
		result.setSuccess(true);
		
		return returnJson(result, userJsonList, model);
	}
	
	// 팔로어 목록
	@RequestMapping(value = "{username}/followers", method = RequestMethod.GET)
	public ResponseEntity<String> followers(@PathVariable String username, Model model) {
		Result result= new Result();
		
		UserInfo userInfo = UserInfo.findUserInfoByUsername(username);
		List<String> userJsonList=followerService.getFollowerJsonListByUserInfo(userInfo);
		result.setSuccess(true);
		
		return returnJson(result, userJsonList, model);
	}
	
	// 글삭제
	// 인증 필요
	@RequestMapping(value = "article/{id}", method = RequestMethod.DELETE) // 글 제거
	public ResponseEntity<String> deleteArticle(String secureKey, @PathVariable Long id, Model model,Article article, BindingResult bindingResult) {
		Result result = new Result();
		
		Result authResult = authCheck(secureKey, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		article = Article.findArticle(id);
		
		// 작성자와 로그인 유저가 같은지 판단하는 코드
		if (!bindingResult.hasFieldErrors("writerId") && !article.getWriterId().equals(authManager.getUserId(secureKey))) {
			bindingResult.rejectValue("writerId", "Equals.article.writerId", "작성자가 다릅니다.");
		}
		
		if(errorCheck(result, bindingResult)){
			articleService.deleteArticle(article);
			
			// 성공~!
			result.setSuccess(true);
		}
		
		return returnJson(result, model);
	}
	
	// 댓글 목록
	@RequestMapping(value = "article/{articleId}/comments", method = RequestMethod.GET) // 댓글 목록
	public ResponseEntity<String> comments(@PathVariable Long articleId, Model model) {	
		Result result=new Result();
		
		List<String> commentJsonList=commentService.getCommentJsonList(articleId);
		Collections.reverse(commentJsonList);
		result.setSuccess(true);
		
		return returnJson(result,commentJsonList,model);
	}
	
	// 댓글 등록
	// 인증 필요
	@RequestMapping(value = "article/{articleId}/comment", method = RequestMethod.POST) // 댓글 등록
	public ResponseEntity<String> comment(@PathVariable Long articleId,Comment comment,BindingResult bindingResult, Model model) {
		Result result = new Result();
		
		Result authResult = authCheck(comment, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		UserInfo userInfo = authManager.getUserInfo(comment.getSecureKey());

		comment.setWriterId(userInfo.getId());
		comment.setWriterUsername(userInfo.getUsername());
		comment.setWriterNickname(userInfo.getNickname());
		comment.setTargetArticleId(articleId);
		comment.setEnable(true);
		
		if (errorCheck(result, bindingResult)) {
			comment.setWriteDate(new Date());
			
			// 저장
			commentService.saveComment(comment);
			
			// 성공~!
			result.setSuccess(true);
		}
		return returnJson(result, comment, model);
	}
	
	// 댓글 삭제
	// 인증 필요
	@RequestMapping(value = "comment/{id}", method = RequestMethod.DELETE) // 댓글 삭제
	public ResponseEntity<String> deleteComment(String secureKey, @PathVariable Long id, Model model,@Valid Comment comment, BindingResult bindingResult) {
		Result result = new Result();
		
		Result authResult = authCheck(secureKey, model);
		if (authResult != null) {
			return returnJson(authResult, model);
		}
		
		comment=Comment.findComment(id);
		
		// 작성자와 로그인 유저가 같은지 판단하는 코드
		if (!bindingResult.hasFieldErrors("writerId") && !comment.getWriterId().equals(authManager.getUserId(secureKey))) {
			bindingResult.rejectValue("writerId", "Equals.comment.writerId", "작성자가 다릅니다.");
		}
		
		if(errorCheck(result, bindingResult)){
			commentService.deleteComment(comment);
			
			// 성공~!
			result.setSuccess(true);
		}
		
		return returnJson(result, model);
	}
	
	@RequestMapping(value = "delete/test", method = RequestMethod.DELETE) // DELETE 테스트
	public void deleteTest(UserInfo userInfo, Model model, HttpServletRequest request) {
		System.out.println(userInfo.getUsername());
		model.addAttribute("result", "");
	}
	
	@RequestMapping(value = "put/test", method = RequestMethod.PUT) // PUT 테스트
	public void putTest(UserInfo userInfo, Model model, HttpServletRequest request) {
		System.out.println(userInfo.getUsername());
		model.addAttribute("result", "");
	}
	
}
