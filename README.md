# spring-gift-enhancement

## 기능 요구 사항

- 상품 정보에 카테고리를 추가한다. 상품과 카테고리 모델 간의 관계를 고려하여 설계하고 구현한다.
- 상품에는 항상 하나의 카테고리가 있어야 한다.
    - 상품 카테고리는 수정할 수 있다.
    - 관리자 화면에서 상품을 추가할 때 카테고리를 지정할 수 있다.
- 카테고리는 1차 카테고리만 있으며 2차 카테고리는 고려하지 않는다.
- 카테고리의 예시는 아래와 같다.
    - 교환권, 상품권, 뷰티, 패션, 식품, 리빙/도서, 레저/스포츠, 아티스트/캐릭터, 유아동/반려, 디지털/가전, 카카오프렌즈, 트렌드 선물, 백화점, ...
- 아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.

### Request

```
GET /api/categories HTTP/1.1
```

### Response

```
HTTP/1.1 200
Content-Type: application/json

[
  {
  "id": 91,
  "name": "교환권",
  "color": "#6c95d1",
  "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
  "description": ""
  }
]
```

## 프로그래밍 요구 사항

- 구현한 기능에 대해 적절한 테스트 전략을 생각하고 작성한다.

## 힌트

- 아래의 DDL을 보고 유추한다.

```sql
create table category
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB

create table product
(
    price       integer      not null,
    category_id bigint       not null,
    id          bigint       not null auto_increment,
    name        varchar(15)  not null,
    image_url   varchar(255) not null,
    primary key (id)
) engine=InnoDB

alter table category
    add constraint uk_category unique (name)

alter table product
    add constraint fk_product_category_id_ref_category_id
        foreign key (category_id)
            references category (id)
```

## 구현 기능 목록

1. Category 테이블을 생성한다.
    - id, name 두가지 값을 가지고 있다.
    - id값은 auto_increment를 사용한다.
    - name값은 unique한 값을 사용한다.
2. Product 테이블에서 Category 테이블을 참조할 수 있도록 설정한다.
    - 카테고리는 하나의 값만을 갖는다.
3. Product 객체의 변화에 따른 테스트 코드를 작성한다.