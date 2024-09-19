package com.membership.Control;

import com.membership.Dto.MemberForm;
import com.membership.Dto.PwChange;
import com.membership.Dto.UserInfo;
import com.membership.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberControl {
    private JavaMailSender javaMailSender;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 페이지
    @GetMapping("/signIn")
    public String login(Model model) {
        return "member/login";
    }


    // 아이디찾기 페이지
    @GetMapping("/findId")
    public String findId(Model model) {
        return "member/findId";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/findPw")
    public String findPw(Model model) {
        return "member/findPw";
    }

    //회원가입 페이지
    @GetMapping("/signUp")
    public String signUp(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/signUp";
    }

    // 회원가입 요청
    @PostMapping("/signUp")
    public String join(@Valid MemberForm memberForm,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) { //유효하지 않은 값 존재
            return "member/signUp";
        }
        try {
            memberService.saveMember(memberForm, passwordEncoder);
        } catch (IllegalStateException e1) {
            bindingResult.rejectValue("userId", "error.userId", e1.getMessage());
            return "member/signUp";
        } catch (IllegalArgumentException e2) {
            bindingResult.rejectValue("email", "error.memberForm", e2.getMessage());
            return "member/signUp";
        }

        return "redirect:/member/signIn";

    }

    // 로그인 실패 - 아이디나 비밀번호 틀린경우
    @GetMapping("/signIn/error")
    public String loginFail(Model model){
        model.addAttribute("loginFailMsg","아이디 또는 비밀번호가 올바르지 않습니다.");
        return "member/login";
    }

    //유저 정보 조회
    @GetMapping("/userInfo/{userName}")
    public String userInfo(@PathVariable("userName") String userName, Model model, Principal principal){
        //비정상 접근시 메인화면으로 이동
        if(!Objects.equals(principal.getName(), userName)){
            return "redirect:/";
        }
        UserInfo userInfo = memberService.getUserInfo(principal.getName());
        model.addAttribute("userInfo",userInfo);
        return "member/userInfo";
    }

    @GetMapping("/userInfo/error/{userName}")
    public String userInfoError(@PathVariable("userName") String userName, Model model){
        // 플래시 속성에서 userInfo를 가져옴, 없을 경우 새로운 객체 생성
        if (!model.containsAttribute("userInfo")) {
            model.addAttribute("userInfo", new UserInfo());
        }
        return "member/userInfo";
    }

    //회원정보 수정
    @PostMapping("/userInfo/update")
    public String userInfoUpdate(@Valid UserInfo userInfo, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        String userName = userInfo.getUserId();

        if (bindingResult.hasErrors()) {
            // 유효성 검사 에러 발생 시 플래시 속성에 에러 정보와 폼 데이터를 추가
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userInfo", bindingResult);
            redirectAttributes.addFlashAttribute("userInfo", userInfo);
            return "redirect:/member/userInfo/error/"+userName;
        }
        memberService.userInfoUpdate(userInfo);
        return "redirect:/member/userInfo/"+userName;
    }
    //비밀번호 변경 페이지
    @GetMapping("/pwChange")
    public String showChangePasswordForm(Model model, Principal principal) {
        PwChange pwChange = new PwChange();
        pwChange.setUserId(principal.getName());
        model.addAttribute("pwChange", pwChange);
        return "member/pwChange";
    }

    //비밀번호 변경
    @PostMapping("/pwChange")
    public String changePassword(@Valid @ModelAttribute PwChange pwChange, BindingResult bindingResult ,Model model) {
        if (bindingResult.hasErrors()) { //유효하지 않은 값 존재
            return "member/pwChange";
        }
        try {
            memberService.changePassword(pwChange, passwordEncoder);
            return "redirect:/member/signIn";
        }catch (IllegalArgumentException e) {
            bindingResult.rejectValue("currentPassword", "error.currentPassword", e.getMessage());
            return "/member/pwChange";
        }catch (IllegalStateException e) {
            bindingResult.rejectValue("confirmNewPassword", "error.confirmNewPassword", e.getMessage());
            return "/member/pwChange";
        }catch (NullPointerException e) {
            bindingResult.rejectValue("newPassword", "error.newPassword", e.getMessage());
            return "/member/pwChange";
        }
    }
}
