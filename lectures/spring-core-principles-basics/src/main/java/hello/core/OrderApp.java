package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.order.Order;
import hello.core.order.OrderService;

public class OrderApp {

    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();

        MemberService memberService = appConfig.memberService();
        OrderService orderService = appConfig.orderService();

        Long memberId = 1L;
        Member member = new Member(memberId, "member A", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "item A", 10000);

        System.out.println("order = " + order); // soutv 치고 탭 누르면 변수명이랑 같이 출력할 수 있음!
        // System.out.println("order.calculatePrice = " + order.calculatePrice());
    }
}
