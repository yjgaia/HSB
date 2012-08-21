package kr.swmaestro.hsb;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import kr.swmaestro.hsb.auth.Auth;
import kr.swmaestro.hsb.auth.AuthManager;
import kr.swmaestro.hsb.auth.AuthUserInfo;
import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.ErrorInfo;
import kr.swmaestro.hsb.model.Follower;
import kr.swmaestro.hsb.model.Result;
import kr.swmaestro.hsb.model.SecureKeyModel;
import kr.swmaestro.hsb.model.UserInfo;
import kr.swmaestro.hsb.service.ArticleService;
import kr.swmaestro.hsb.service.FollowerService;
import kr.swmaestro.hsb.service.UserService;
import kr.swmaestro.hsb.util.PasswordEncoder;
import kr.swmaestro.hsb.util.article.ArticleUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	private FollowerService followerService;
	
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
	
	// 결과값 반환
	private void ret(Result result, SecureKeyModel data, Model model) {
		data.setSecureKey(null); // 보안 키 제거
		result.setSingle(true);
		result.setData(data);
		model.addAttribute("result", result);
	}
	
	// 결과값 반환
	private void ret(Result result, List list, Model model) {
		result.setSingle(false);
		result.setList(list);
		model.addAttribute("result", result);
	}
	
	// 인증처리
	private boolean authCheck(String secureKey, Model model) {
		if (authManager.isAuthenticated(secureKey)) {
			return true;
		} else {
			Result result = new Result();
			ErrorInfo error = new ErrorInfo();
			error.setCode("NeedAuth");
			error.setDefaultMessage("인증이 필요합니다.");
			Set<ErrorInfo> errors = new HashSet<>();
			errors.add(error);
			result.setSuccess(false);
			result.setErrors(errors);
			model.addAttribute("result", result);
			return false;
		}
	}
	
	// 인증처리
	private boolean authCheck(SecureKeyModel secureKeyModel, Model model) {
		return authCheck(secureKeyModel.getSecureKey(), model);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String main(Model model) {
		return "main";
	}
	
	@RequestMapping(value = "admin/test", method = RequestMethod.GET)
	public void test(String secureKey, Model model) {
		if (authCheck(secureKey, model)) {
			System.out.println("TEST 페이지 실행");
		}
	}
	
	// 로그인
	@RequestMapping(value = "user/auth", method = RequestMethod.POST) // 인증 생성
	public void login(@Valid AuthUserInfo authUserInfo, BindingResult bindingResult, Model model, HttpServletRequest request) {
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
		ret(result, authUserInfo, model);
	}
	
	// 로그아웃
	@Auth // 인증 필요
	@RequestMapping(value = "user/auth", method = RequestMethod.DELETE) // 인증 제거
	public void logout(Model model) {}
	
	// 회원가입
	@RequestMapping(value = "user/account", method = RequestMethod.POST)
	public void join(@Valid UserInfo userInfo, BindingResult bindingResult, Model model) {
		Result result = new Result();
		
		if (!bindingResult.hasFieldErrors("password") && !userInfo.getPassword().equals(userInfo.getPasswordConfirm())) {
			bindingResult.rejectValue("password", "Equals.userInfo.passwordConfirm", "비밀번호와 비밀번호 확인이 다릅니다.");
		}
		if (!bindingResult.hasFieldErrors("username") && UserInfo.existsUser(userInfo.getUsername())) {
			bindingResult.rejectValue("username", "Exists.userInfo.username", "이미 존재하는 아이디입니다.");
		}
		if (!bindingResult.hasFieldErrors("nickname") && UserInfo.existsNickname(userInfo.getNickname())) {
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
		ret(result, userInfo, model);
	}
	
	// 회원 정보 수정
	@Auth // 인증 필요
	@RequestMapping(value = "user/account", method = RequestMethod.PUT)
	public void updateAccount(@PathVariable String username, Model model) {}
	
	// 회원 정보 삭제 (탈퇴)
	@Auth // 인증 필요
	@RequestMapping(value = "user/account", method = RequestMethod.DELETE)
	public void leave(@PathVariable String username, Model model) {}
	
	// 타임라인
	@Auth // 인증 필요
	@RequestMapping(value = "user/timeline", method = RequestMethod.GET)
	public void timeline(Model model) {
	}
	
	// 회원의 모든 정보 + 글 보기
	@RequestMapping(value = "{username}", method = RequestMethod.GET)
	public String home(@PathVariable String username, String secureKey, Model model) {
		Result result = new Result();
		
		UserInfo userInfo = UserInfo.findUserInfoByUsername(username);
		List<Article> articleList = articleService.findArticlesByWriterId(UserInfo.findUserInfoByUsername(username).getId(), 0l, 10);
		System.out.println(articleList.size());
		
		ret(result, articleList, model);
		
		return "home";
	}
	
	// 글쓰기
	@RequestMapping(value = "{username}", method = RequestMethod.POST)
	public String write(@PathVariable String username, @Valid Article article, BindingResult bindingResult, Model model) {
		Result result = new Result();
		
		if (authCheck(article, model)) {
			UserInfo userInfoFromSession=authManager.getUserInfo(article.getSecureKey());
			article=ArticleUtil.setArticleInfoWithSession(article, userInfoFromSession);
			System.out.println(article.getWriterId());
			if (errorCheck(result, bindingResult)) {
				article.setWriteDate(new Date());
				
				// 저장
				articleService.saveArticle(article);
				
				// 성공~!
				result.setSuccess(true);
			}
			ret(result, article, model);
		}
		
		return "home";
	}

	// 팔로우하기
	@Auth // 인증 필요
	@RequestMapping(value = "{username}/follow", method = RequestMethod.POST) // 팔로우 생성
	public String follow(@PathVariable String username, @Valid Follower follower, BindingResult bindingResult, Model model) {
		Result result= new Result();
		
		if(authCheck(follower,model)){
			UserInfo followerUser=authManager.getUserInfo(follower.getSecureKey());
			UserInfo followedUser = UserInfo.findUserInfoByUsername(username);
			
			//본인의 계정은 팔로우 할 수 없도록 validation
			if (!bindingResult.hasFieldErrors("userId") && followerUser.getId().equals(followedUser.getId()) ){
				bindingResult.rejectValue("userId", "Equals.follower.userid", "본인의 아이디는 팔로우 할 수 없습니다.");
			}
			if (errorCheck(result, bindingResult)) {
				follower.setUserId(followedUser.getId());
				follower.setFollowerId(followerUser.getId());
				follower.setFollowDate(new Date());
			
				followerService.saveFollower(follower);
			
				result.setSuccess(true);
			}
			ret(result,follower,model);
		}
		
		return "home";
		
	}
	
	// 언팔로우
	@Auth // 인증 필요
	@RequestMapping(value = "{username}/follow", method = RequestMethod.DELETE) // 팔로우 제거
	public void unfollow(@PathVariable String username, Model model) {}
	
	// 팔로잉 목록
	@RequestMapping(value = "{username}/following", method = RequestMethod.GET)
	public void following(@PathVariable String username, Model model) {}
	
	// 팔로어 목록
	@RequestMapping(value = "{username}/followers", method = RequestMethod.GET)
	public void followers(@PathVariable String username, Model model) {}
	
	// 글삭제
	@Auth // 인증 필요
	@RequestMapping(value = "article/{id}", method = RequestMethod.DELETE) // 글 제거
	public void deleteArticle(@PathVariable Long id, Model model) {}
	
	// 댓글 등록
	@Auth // 인증 필요
	@RequestMapping(value = "article/{articleId}/comment", method = RequestMethod.POST) // 댓글 등록
	public void comment(@PathVariable Long articleId, Model model) {}
	
	// 댓글 삭제
	@Auth // 인증 필요
	@RequestMapping(value = "comment/{id}", method = RequestMethod.DELETE) // 댓글 삭제
	public void deleteComment(@PathVariable Long id, Model model) {}
	
}
