package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.controller.dto.MemberDTO;
import com.jpabook.jpashop.controller.form.MemberForm;
import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
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

        Member member = Member.builder()
                .name(form.getName())
                .address(
                        Address.builder()
                                .city(form.getCity())
                                .street(form.getStreet())
                                .zipcode(form.getZipcode())
                                .build()
                )
                .build();

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String members(Model model) {
        List<MemberDTO> memberDTOs = memberService.findAll()
                .stream()
                .map(member -> MemberDTO.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .address(member.getAddress())
                        .build()
                )
                .collect(Collectors.toList());
        model.addAttribute("members", memberDTOs);
        return "members/memberList";
    }
}
