package kr.swmaestro.hsb;

import kr.swmaestro.hsb.domain.UserInfo;
import kr.swmaestro.hsb.model.RooTestModel;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

}
