package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(new Address(form.getCity(),
                form.getStreet(),
                form.getZipcode()));

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String members(Model model) {
        List<MemberDTO> memberDTOs = memberService.findAll().stream()
                .map(member -> {
                    return MemberDTO.builder()
                            .id(member.getId())
                            .name(member.getName())
                            .address(member.getAddress())
                            .build();
                }).collect(Collectors.toList());
        model.addAttribute("members", memberDTOs);
        return "members/memberList";
    }
}
