package kr.swmaestro.hsb;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import kr.swmaestro.hsb.auth.Auth;
import kr.swmaestro.hsb.data.DataCenter;
import kr.swmaestro.hsb.model.ErrorInfo;
import kr.swmaestro.hsb.model.ResultModel;
import kr.swmaestro.hsb.model.RooTestModel;
import kr.swmaestro.hsb.model.UserInfo;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class Controller {
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public void test(UserInfo userInfo, BindingResult bindingResult, Model model) {
		RooTestModel rooTestModel = new RooTestModel();
		rooTestModel.setMsg("message");
		model.addAttribute("result", rooTestModel);
	}

	
	// 오류 체크
	private boolean errorCheck(ResultModel resultModel, BindingResult bindingResult) {
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
				for (int i = 1 ; i < objectError.getArguments().length ; i++) {
					arguments.add(objectError.getArguments()[i]);
				}
				error.setArguments(arguments);
				errors.add(error);
			}
			resultModel.setErrors(errors);
			resultModel.setSuccess(false);
			return false;
		}
		return true;
	}
	
	// 결과값 반환
	private void ret(ResultModel resultModel, Model model, HttpServletRequest request) {
		
		// URL에 파라미터 붙히기
		String Url = request.getRequestURL().toString();
		Enumeration<?> param = request.getParameterNames();
		if (param != null) {
			String strParam = null;
			while (param.hasMoreElements()) {
				if (strParam == null) {
					strParam = "?";
				} else {
					strParam += "&";
				}
				String name = (String) param.nextElement();
				String value = request.getParameter(name);
				strParam += name + "=" + value;
			}
			if (strParam != null) {
				Url += strParam;
			}
		}
        
		resultModel.setUrl(Url);
		resultModel.setSecureKey(null); // 보안 키 제거
		resultModel.setReturnDate(new Date()); // 서버측 반환 시간 설정
		model.addAttribute("result", resultModel);
	}
	
	// 로그인
	@RequestMapping(value = "user/auth", method = RequestMethod.POST) // 인증 생성
	public void login(BindingResult bindingResult, Model model) {
		
	}
	
	// 로그아웃
	@Auth // 인증 필요
	@RequestMapping(value = "user/auth", method = RequestMethod.DELETE) // 인증 제거
	public void logout(Model model) {}
		
	// 회원가입
	@RequestMapping(value = "user/account", method = RequestMethod.GET) // 테스트용
	//@RequestMapping(value = "user/account", method = RequestMethod.POST)
	public void join(@Valid UserInfo userInfo, BindingResult bindingResult, Model model, HttpServletRequest request) {
		if (errorCheck(userInfo, bindingResult)) {
			DataCenter.save(userInfo);
		}
		ret(userInfo, model, request);
	}
	
	// 회원 정보 수정
	@Auth // 인증 필요
	@RequestMapping(value = "user/account", method = RequestMethod.PUT)
	public void updateAccount(@PathVariable String username, Model model) {}
	
	// 회원 정보 삭제 (탈퇴)
	@Auth // 인증 필요
	@RequestMapping(value = "user/account", method = RequestMethod.DELETE)
	public void leave(@PathVariable String username, Model model) {}
	
	// 회원의 모든 정보 + 글 보기
	@RequestMapping(value = "{username}", method = RequestMethod.GET)
	public void view(@PathVariable String username, Model model) {}
	
	// 타임라인
	@Auth // 인증 필요
	@RequestMapping(method = RequestMethod.GET)
	public void timeline(Model model) {}
	
	// 글쓰기
	@Auth // 인증 필요
	@RequestMapping(method = RequestMethod.POST)
	public void write(Model model) {}

	// 팔로우하기
	@Auth // 인증 필요
	@RequestMapping(value = "{username}/follow", method = RequestMethod.POST) // 팔로우 생성
	public void follow(@PathVariable String username, Model model) {}
	
	// 언팔로우
	@Auth // 인증 필요
	@RequestMapping(value = "{username}/follow", method = RequestMethod.DELETE) // 팔로우 제거
	public void unfollow(@PathVariable String username, Model model) {}
	
	// 글삭제
	@Auth // 인증 필요
	@RequestMapping(value = "article/{id}", method = RequestMethod.DELETE) // 글 제거
	public void deleteArticle(@PathVariable Long id, Model model) {}
	
}
