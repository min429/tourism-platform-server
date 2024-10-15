// 이전 페이지로 이동하는 함수 또는 웹뷰 종료 함수
import { baseUrl } from './global.js';

// 이전 페이지로 이동하는 함수 또는 웹뷰 종료 함수
export function goBackOrClose() {
    const currentUrl = window.location.href;

    // // 현재 URL이 /api/boards/thumbnail로 시작하는지 확인
    // if (currentUrl.startsWith('http://localhost:8080/api/boards/thumbnail')) {
    //     // Unity로 메시지 보내기 (웹뷰 종료 요청)
    //     Unity.call('closeWebView');
    // } else {
    //     // 이전 페이지로 이동
    //     window.history.back();
    // }

    window.history.back();
}

// x 버튼에 이벤트 리스너 추가
// DOM이 완전히 로드된 후에 이벤트 리스너를 추가
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.close-button').forEach(button => {
        button.addEventListener('click', goBackOrClose);
    });
});