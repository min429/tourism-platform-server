import {baseUrl} from "./global.js";

$(document).ready(function () {
    let selectedImages = [];  // 선택된 이미지들을 저장할 배열
    let tags = [];  // 태그를 저장할 배열
    let type = '';  // 선택된 카테고리를 저장할 변수
    let address = '';  // 선택된 주소를 저장할 변수

    // URL에서 파라미터 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    let area = urlParams.get('area');  // 'area' 값 추출

    // 사진 첨부하기 (카메라 촬영)
    $("#attach-photo").on("click", function () {
        let fileInput = $('<input type="file" accept="image/*" capture="camera">');  // 카메라 촬영을 위한 input 생성
        fileInput.trigger('click');  // 파일 선택 창을 띄움

        // 파일이 선택되면 처리
        fileInput.on("change", function (event) {
            const file = event.target.files[0];
            if (file) {
                addImagePreview(file);
            }
        });
    });

    // 사진 가져오기 (갤러리에서 선택)
    $("#select-photo").on("click", function () {
        let fileInput = $('<input type="file" accept="image/*" multiple>');  // 여러 이미지를 선택할 수 있도록 설정
        fileInput.trigger('click');

        fileInput.on("change", function (event) {
            let files = event.target.files;
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                if (!selectedImages.includes(file)) {  // 중복 이미지 방지
                    addImagePreview(file);
                }
            }
        });
    });

    // 이미지 추가 기능
    function addImagePreview(file) {
        let reader = new FileReader();  // FileReader 객체 생성
        reader.onload = function (e) {
            let imgElement = $(`
                <div class="review-image-thumbnail position-relative me-2 bg-color">
                    <img class="rounded" src="${e.target.result}" alt="Image" style="width: 100px; height: 100px;">
                    <button class="image-x-button btn position-absolute top-0 end-0 p-0">
                        <img src="/assets/icons/image-x-button.png" style="width: 18px; height: 18px;">
                    </button>
                </div>
            `);

            // X 버튼 클릭 시 이미지 삭제
            imgElement.find("button").on("click", function () {
                imgElement.remove();
                selectedImages = selectedImages.filter(img => img !== file);  // 배열에서 제거
            });

            $("#image-preview").append(imgElement);
        };
        reader.readAsDataURL(file);  // 파일을 읽어와 미리보기
        selectedImages.push(file);  // 배열에 이미지 추가
    }

    // 카테고리 버튼 클릭 시 선택 처리
    $("#spot-button, #restaurant-button, #accommodation-button").on("click", function () {
        $("#spot-button, #restaurant-button, #accommodation-button").removeClass("active");
        $(this).addClass("active");

        if (this.id === "spot-button") {
            type = 'SPOT';
        } else if (this.id === "restaurant-button") {
            type = 'RESTAURANT';
        } else if (this.id === "accommodation-button") {
            type = 'ACCOMMODATION';
        }
    });

    // 태그 추가 기능
    window.addTag = function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            let tag = `${$("#tag-input").val().trim()}`;

            if (tag && !tags.includes(tag)) {
                tags.push(tag);

                let tagElement = $(`
                    <span class="small bg-white border rounded-4 ps-2 py-0 me-2 text-secondary d-flex align-items-center flex-shrink-0">
                        ${tag}
                        <button class="tag-button btn border-0 p-0 mx-1 text-secondary">
                            <i class="bi bi-x"></i>
                        </button>
                    </span>
                `);

                tagElement.find("button").on("click", function () {
                    tagElement.remove();
                    tags = tags.filter(t => t !== tag);
                });

                $("#tag-list").append(tagElement);
                $("#tag-input").val("");
            }
        }
    };

    // 지도 렌더링 함수
    function renderMap(addr) {
        if (!addr) return;  // 주소가 없는 경우 렌더링하지 않음

        // 네이버 지도 API 로드 확인 및 지도 객체 생성을 동기화
        function initializeMap() {
            // 지오코딩 실행
            naver.maps.Service.geocode({ query: addr }, function (status, response) {
                if (status === naver.maps.Service.Status.OK && response.v2.addresses.length > 0) {
                    let item = response.v2.addresses[0];
                    let location = new naver.maps.LatLng(item.y, item.x);

                    // 지오코딩 성공 후 지도 객체 생성
                    let map = new naver.maps.Map('map-preview', {  // 'map' 변수를 여기에서 선언
                        center: location,  // 입력한 주소 위치로 지도 중심 설정
                        zoom: 17
                    });

                    // 지도에 마커 추가
                    let marker = new naver.maps.Marker({
                        position: location,
                        map: map
                    });

                    // 정보 창 설정
                    let infoWindow = new naver.maps.InfoWindow({
                        content: `<div class="info-box shadow p-2 bg-light">
                        <h5 class="font-hanna-pro mb-1 font-weight-bold text-dark">주소 정보</h5>
                        <div class="font-hanna-air text-muted small">도로명 주소: ${item.roadAddress || '없음'}</div>
                        <div class="font-hanna-air text-muted small">지번 주소: ${item.jibunAddress || '없음'}</div>
                    </div>`
                    });

                    // 마커 클릭 시 정보 창 표시
                    naver.maps.Event.addListener(marker, "click", function () {
                        infoWindow.open(map, marker);
                    });

                    // 지도 클릭 시 정보 창 닫기
                    naver.maps.Event.addListener(map, "click", function () {
                        infoWindow.close();
                    });
                } else {
                    // 지오코딩 실패 또는 주소를 찾지 못한 경우
                    alert('해당하는 주소를 찾을 수 없습니다.');
                }
            });
        }

        // 네이버 지도 API가 이미 로드되었는지 확인하고, 로드되었다면 바로 지도 초기화
        if (typeof naver !== 'undefined' && typeof naver.maps !== 'undefined') {
            initializeMap();
        } else {
            // 네이버 지도 API 로드를 대기하고 로드 완료 후 지도 초기화
            var mapInterval = setInterval(function () {
                if (typeof naver !== 'undefined' && typeof naver.maps !== 'undefined') {
                    clearInterval(mapInterval);
                    initializeMap();
                }
            }, 100); // 100ms 간격으로 로드 여부 체크
        }
    }

    // 주소 등록 기능 (네이버 지도 팝업 띄우기)
    $("#register-address").on("click", function () {
        let addr = prompt("주소를 입력해주세요.");
        registerMap(addr);
    });

    function registerMap(addr){
        if (!addr) {
            return;  // 주소가 없는 경우 렌더링하지 않음
        }
        address = addr;  // 주소 저장
        renderMap(address);  // 입력한 주소로 지도 렌더링
    }

    // 작성 완료 버튼 클릭 시 데이터 전송 (AJAX 사용하지 않음)
    $("#submit-post").on("click", function () {
        if (!type) {
            alert("카테고리를 선택해주세요.");
            return;
        }

        let title = $("#title-input").val().trim();
        if (!title) {
            alert("제목을 입력해주세요.");
            return;
        }

        // FormData 객체 생성
        let formData = new FormData();
        formData.append("title", title);
        formData.append("description", $("#description").val());
        formData.append("address", address);

        // 태그 추가
        tags.forEach(tag => {
            formData.append("tags", tag);
        });

        // 선택된 이미지들 추가
        selectedImages.forEach((image, index) => {
            formData.append("images", image);  // 파일을 FormData에 직접 추가
        });

        // 폼 데이터를 서버로 전송
        $.ajax({
            url: `${baseUrl}/user/boards/new/${type}?area=${area}`,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (response) {
                if (response.redirectUrl) {
                    window.history.back();
                }
            },
            error: function (error) {
                // 전송 실패 시 처리
                console.error('Error creating post', error);
            }
        });
    });
});
