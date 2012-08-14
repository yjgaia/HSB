package kr.swmaestro.hsb;

import javax.validation.Valid;

import kr.swmaestro.hsb.auth.Auth;
import kr.swmaestro.hsb.model.UserInfo;
import kr.swmaestro.hsb.model.RooTestModel;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class Controller {
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public void test(UserInfo userInfo, BindingResult bindingResult, Model model) {
		RooTestModel rooTestModel = new RooTestModel();
		rooTestModel.setMom("dad");
		rooTestModel.setMsg("message");
		model.addAttribute("result", rooTestModel);
	}

	
	
	
	// 로그인
	@RequestMapping(value = "signin", method = RequestMethod.POST)
	public void signin(Model model) {}
	
	// 회원가입
	//@RequestMapping(value = "signup", method = RequestMethod.POST)
	@RequestMapping(value = "signup", method = RequestMethod.GET)
	public void signup(@Valid UserInfo userInfo, BindingResult bindingResult, Model model) {
		model.addAttribute("result", userInfo);
	}
	
	// 로그아웃
	@RequestMapping(value = "signout", method = RequestMethod.POST)
	public void signout(Model model) {}
	
	// 회원 정보 보기
	@RequestMapping(value = "user/{username}", method = RequestMethod.GET)
	public void userInfo(@PathVariable String username, Model model) {}
	
	// 회원 정보 수정
	@Auth
	@RequestMapping(value = "user/{username}", method = RequestMethod.PUT)
	public void updateUserInfo(@PathVariable String username, Model model) {}
	
	// 회원 정보 삭제 (탈퇴)
	@Auth
	@RequestMapping(value = "user/{username}", method = RequestMethod.DELETE)
	public void deleteUserInfo(@PathVariable String username, Model model) {}
	
	// 팔로우하기
	@Auth
	@RequestMapping(value = "user/{username}/follow", method = RequestMethod.POST)
	public void follow(@PathVariable String username, Model model) {}
	
	// 언팔로우
	@Auth
	@RequestMapping(value = "user/{username}/follow", method = RequestMethod.DELETE)
	public void unfollow(@PathVariable String username, Model model) {}
	
	// 유저의 글 목록 보기
	@RequestMapping(value = "user/{username}/articles", method = RequestMethod.GET)
	public void articles(@PathVariable String username, Model model) {}
	
	// 내 팔로잉 목록 보기
	@Auth
	@RequestMapping(value = "following", method = RequestMethod.GET)
	public void following(Model model) {}
	
	// 나를 팔로우 하는 사람 목록 보기
	@Auth
	@RequestMapping(value = "followers", method = RequestMethod.GET)
	public void followers(Model model) {}
	
	// 글쓰기
	@Auth
	@RequestMapping(value = "write", method = RequestMethod.POST)
	public void write(Model model) {}
	
	// 글보기 (댓글도 같이 가져옵니다.)
	@RequestMapping(value = "article/{id}", method = RequestMethod.GET)
	public void article(@PathVariable Long id, Model model) {}
	
	// 글수정
	@Auth
	@RequestMapping(value = "article/{id}", method = RequestMethod.PUT)
	public void updateArticle(@PathVariable Long id, Model model) {}
	
	// 글삭제
	@Auth
	@RequestMapping(value = "article/{id}", method = RequestMethod.DELETE)
	public void deleteArticle(@PathVariable Long id, Model model) {}
	
	// 댓글달기
	@Auth
	@RequestMapping(value = "article/{id}/comment", method = RequestMethod.POST)
	public void comment(@PathVariable Long id, Model model) {}
	
	// 댓글수정
	@Auth
	@RequestMapping(value = "comment/{id}", method = RequestMethod.PUT)
	public void updateComment(@PathVariable Long id, Model model) {}
	
	// 댓글삭제
	@Auth
	@RequestMapping(value = "comment/{id}", method = RequestMethod.DELETE)
	public void deleteComment(@PathVariable Long id, Model model) {}
	
	// 타임라인
	@Auth
	@RequestMapping(value = "timeline", method = RequestMethod.GET)
	public void timeline(Model model) {}

}
