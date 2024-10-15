import { baseUrl } from './global.js';

window.redirectToDetail = function(boardId, isUserBoard, type) {
    const url = `${baseUrl}/boards/detail/${type.toLowerCase()}?boardId=${boardId}&isUserBoard=${isUserBoard}`;
    window.location.href = url;
};

/** header 버튼 **/
// 페이지 경로 변경 함수
window.changePage = function(type) {
    // 현재 URL 가져오기
    const currentUrl = new URL(window.location.href);

    // URL에서 현재 경로(예: /api/user/boards/thumbnail/spot) 가져오기
    const pathname = currentUrl.pathname;

    // 마지막 경로 부분(spot) 교체
    const newPathname = pathname.replace(/\/[^\/]+$/, `/${type}`);

    // area 파라미터만 유지하고 다른 쿼리 파라미터는 제거
    const params = new URLSearchParams(currentUrl.search);
    const area = params.get('area');  // area 파라미터 값 추출

    // URL 경로 갱신
    currentUrl.pathname = newPathname;
    currentUrl.search = ''; // 모든 파라미터 초기화

    // area 파라미터가 있으면 다시 추가
    if (area) {
        currentUrl.searchParams.set('area', area);
    }

    // 새로운 URL로 페이지 이동
    window.location.replace(currentUrl.href);
};

// 각 버튼에 이벤트 리스너 추가
document.getElementById('btn-restaurant').addEventListener('click', function() {
    changePage('RESTAURANT');
});

document.getElementById('btn-accommodation').addEventListener('click', function() {
    changePage('ACCOMMODATION');
});

document.getElementById('btn-spot').addEventListener('click', function() {
    changePage('SPOT');
});

/** 페이징 **/
let userCurrentPage = 1;   // 기본값 1
let pageGroupSize = 5;     // 한 번에 표시할 페이지 번호 그룹 크기

window.onload = function() {
    // URL에서 페이지 번호 정보를 가져옴
    const urlParams = new URLSearchParams(window.location.search);

    // URL 파라미터에서 값을 가져오고 없으면 1로 설정
    const userPage = parseInt(urlParams.get('user_page')) || 1;   // 기본값은 1

    // 현재 페이지 번호 설정
    userCurrentPage = userPage;

    // 페이지 그룹 번호 설정 (현재 페이지 번호에 따른 페이지 그룹 계산)
    const userGroupStart = Math.floor((userCurrentPage - 1) / pageGroupSize) * pageGroupSize + 1;

    // 페이지 번호 버튼 렌더링
    updatePageButtons(userGroupStart, userGroupStart + pageGroupSize - 1, 'user');
};

// 페이지 버튼 업데이트 함수
function updatePageButtons(startPage, endPage, type) {
    const pageContainer = document.getElementById(`${type}-page-buttons`);
    pageContainer.innerHTML = ""; // 기존 버튼 초기화

    // 새로운 페이지 번호 버튼 생성
    for (let i = startPage; i <= endPage; i++) {
        const pageButton = document.createElement("button");
        pageButton.className = "border border-0 bg-transparent p-0 text-button-color px-1 mx-1";
        pageButton.textContent = i;

        // 직접 이벤트 핸들러 추가 (setAttribute 대신 addEventListener 사용)
        pageButton.addEventListener('click', function() {
            goToPage(i);
        });

        pageContainer.appendChild(pageButton);
    }
}

// 페이지 이동 함수
window.goToPage = function(userPageNumber) {
    if (userPageNumber !== null) {
        userCurrentPage = userPageNumber; // 유저 페이지 업데이트
    }

    // URL 파라미터 수정
    const url = new URL(window.location.href);
    if (userPageNumber !== null) {
        url.searchParams.set('page', userCurrentPage);
    }

    // 페이지 이동
    window.location.replace(url.href);
};

// 유저 페이지 그룹 변경 함수
window.updateUserPageGroup = function(increment) {
    let newGroupStart = userCurrentPage + (increment * pageGroupSize);
    if (newGroupStart < 1) {
        newGroupStart = 1; // newGroupStart가 음수일 경우 1로 설정
    }
    const newGroupEnd = newGroupStart + pageGroupSize - 1;

    // 유저 페이지 버튼 업데이트
    updatePageButtons(newGroupStart, newGroupEnd, 'user');

    // 새 페이지 그룹의 첫 번째 페이지로 이동
    goToPage(newGroupStart); // 운영자 페이지는 그대로 유지
};
