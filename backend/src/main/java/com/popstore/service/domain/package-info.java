/**
 * 애플리케이션의 핵심 비즈니스 로직을 포함하는 도메인 계층입니다.
 * 
 * <p>이 패키지는 다음과 같은 하위 패키지로 구성됩니다:</p>
 * 
 * <ul>
 *     <li>{@link com.popstore.service.domain.model} - 도메인 엔티티와 값 객체</li>
 *     <li>{@link com.popstore.service.domain.service} - 도메인 서비스 (비즈니스 규칙 및 로직)</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>순수한 비즈니스 로직만을 포함하며, 기술적인 구현 세부사항은 제외합니다.</li>
 *     <li>모든 클래스는 POJO(Plain Old Java Object)로 구현됩니다.</li>
 *     <li>외부 프레임워크나 라이브러리에 대한 의존성을 가지지 않습니다.</li>
 *     <li>도메인 모델은 비즈니스 규칙을 캡슐화하고, 도메인 서비스는 여러 엔티티에 걸친 비즈니스 로직을 처리합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>도메인 모델은 가능한 한 풍부한(rich) 모델로 설계합니다.</li>
 *     <li>비즈니스 규칙은 도메인 모델 내부에 캡슐화합니다.</li>
 *     <li>도메인 서비스는 엔티티나 값 객체에 속하지 않는 비즈니스 로직을 처리할 때만 사용합니다.</li>
 *     <li>도메인 모델의 불변성(immutability)을 보장합니다.</li>
 * </ul>
 * </p>
 */
package com.popstore.service.domain; 