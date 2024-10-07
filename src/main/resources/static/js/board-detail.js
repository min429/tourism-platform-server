/** 모달 **/
// Swiper 초기화
let swiper;

let isDragging = false;
let touchStartX = 0;
let touchEndX = 0;

function initializeSwiper() {
    if (swiper) {
        swiper.destroy(true, true); // Swiper가 이미 초기화된 경우 제거하고 다시 초기화
    }

    swiper = new Swiper('.mySwiper', {
        loop: true,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        grabCursor: true,
        autoplay: {
            delay: 3000,
            disableOnInteraction: false,
        },
        slidesPerView: 1,
        spaceBetween: 10, // 슬라이드 간 여백
        on: {
            // Swiper 초기화 후 슬라이드에 Bootstrap 클래스 추가
            init: function () {
                const slides = document.querySelectorAll('.swiper-slide');
                slides.forEach(slide => {
                    slide.classList.add('d-flex', 'justify-content-center', 'align-items-center');
                });
            },
            // 슬라이드 변경 시에도 Bootstrap 클래스를 적용
            slideChange: function () {
                const slides = document.querySelectorAll('.swiper-slide');
                slides.forEach(slide => {
                    slide.classList.add('d-flex', 'justify-content-center', 'align-items-center');
                });
            }
        }
    });

    const modal = document.getElementById('imageModal');
    const modalContent = document.getElementById('modalContent');

    // 터치 시작 이벤트: 터치 시작 위치 저장
    modal.addEventListener('touchstart', function(event) {
        touchStartX = event.changedTouches[0].clientX;
        isDragging = false; // 드래그 상태를 초기화
    });

    // 터치 이동 이벤트: 드래그 상태로 전환
    modal.addEventListener('touchmove', function(event) {
        isDragging = true; // 터치 이동 중
        touchEndX = event.changedTouches[0].clientX;
    });

    // 터치 종료 이벤트: 터치 이동이 없으면 모달 닫기
    modal.addEventListener('touchend', function(event) {
        if (!isDragging && !modalContent.contains(event.target)) {
            // 터치 이동이 없고 모달 여백을 클릭했을 때 모달 닫기
            $('#imageModal').modal('hide');
        }
    });

    // 마우스 클릭 이벤트: 터치와 마우스 이벤트 분리
    modal.addEventListener('click', function (event) {
        if (!modalContent.contains(event.target)) {
            $('#imageModal').modal('hide'); // 모달 닫기
        }
    });
}

// 클릭된 이미지의 src를 모달 이미지로 설정하고 Swiper로 이동하는 함수
function showImageModal(imageElement) {
    const imageElements = document.querySelectorAll('#detail-images img');
    const currentImageIndex = Array.from(imageElements).indexOf(imageElement);

    // 이미지 리스트 배열 생성
    const imagesArray = Array.from(imageElements).map(img => img.src);

    // Swiper에 동적으로 슬라이드 추가
    const swiperWrapper = document.getElementById('swiperWrapper');
    swiperWrapper.innerHTML = ''; // 기존 슬라이드 삭제

    imagesArray.forEach((src, index) => {
        const slideDiv = document.createElement('div');
        slideDiv.classList.add('swiper-slide');

        const imgElement = document.createElement('img');
        imgElement.src = src;
        imgElement.classList.add('rounded-2');
        imgElement.style.maxHeight = '80vh';
        imgElement.style.maxWidth = '80vw';
        imgElement.style.objectFit = 'contain';

        slideDiv.appendChild(imgElement);
        swiperWrapper.appendChild(slideDiv);
    });

    // 모달을 보여줌
    $('#imageModal').modal('show');

    // Swiper 초기화 (동적 생성 후)
    initializeSwiper();

    // Swiper에서 해당 이미지로 이동
    swiper.slideTo(currentImageIndex + 1); // slideTo는 0 기반이므로 +1
}

/** 수정•삭제 **/
document.addEventListener('DOMContentLoaded', function () {
    // URL에서 isUserBoard 값 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const isUserBoard = urlParams.get('isUserBoard');

    // isUserBoard가 false일 경우 수정, 삭제 버튼 숨기기
    if (isUserBoard === 'false') {
        const editButton = document.getElementById('edit-button');
        const deleteButton = document.getElementById('delete-button');

        if (editButton) {
            editButton.style.display = 'none';
        }

        if (deleteButton) {
            deleteButton.style.display = 'none';
        }
    }
});

// 수정 버튼 클릭 시 API 요청
document.getElementById("edit-button").addEventListener("click", function () {
    // 현재 URL에서 경로 부분을 '/' 기준으로 나눔
    const pathParts = window.location.pathname.split('/');
    const type = pathParts[pathParts.length - 1];  // 'type' 추출

    // URLSearchParams를 사용하여 쿼리 파라미터에서 'boardId' 추출
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get('boardId');  // 'boardId' 값 추출
    const area = urlParams.get('area'); // 'area' 값 추출

    // board-edit 페이지로 이동하면서 type과 boardId를 쿼리 파라미터로 전달
    window.location.href = `/api/user/boards/editForm/${type}?area=${area}&boardId=${boardId}`;
});

// 삭제 버튼 클릭 시 API 요청
document.getElementById("delete-button").addEventListener("click", function () {
    // 현재 URL에서 경로 부분을 '/' 기준으로 나눔
    const pathParts = window.location.pathname.split('/');
    const type = pathParts[pathParts.length - 1];  // 'type' 추출

    // URLSearchParams를 사용하여 쿼리 파라미터에서 'boardId' 추출
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get('boardId');  // 'boardId' 값 추출

    // DELETE 요청을 보낼 URL 생성
    const url = `/api/user/boards/${type}/${boardId}?boardId=${boardId}`;

    // DELETE 요청 전송
    fetch(url, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                window.history.back();
            } else {
                console.error("삭제 실패");
            }
        })
        .catch(error => {
            console.error("에러 발생:", error);
        });
});

/** 지도 **/
function goToMap(placeName) {
    if (!placeName || placeName.trim() === "") {
        // placeName이 없거나 빈 문자열이면 팝업창 띄우기
        alert("주소가 등록되지 않았습니다.");
    } else {
        // placeName이 유효하면 URL 인코딩 후 지도 페이지로 이동
        const encodedName = encodeURIComponent(placeName); // 검색어 URL 인코딩
        const url = `https://map.naver.com/p/search/${encodedName}`;
        window.open(url, '_self');
    }
}
