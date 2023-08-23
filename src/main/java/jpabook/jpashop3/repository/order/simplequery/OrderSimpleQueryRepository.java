package jpabook.jpashop3.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import jpabook.jpashop3.repository.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public Result findOrderDtos() {
        List<OrderSimpleQueryDto> result = em.createQuery(
                "select new jpabook.jpashop3.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
        return new Result(result);
    }
}
