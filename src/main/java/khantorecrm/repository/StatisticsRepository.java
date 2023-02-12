package khantorecrm.repository;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.payload.dao.projection.ClientListBySumAmount;
import khantorecrm.payload.dao.projection.ProductListAboutInput;
import khantorecrm.payload.dao.projection.ProductListBySumAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<BaseEntity, Long> {
    @Query(
            value = "select sum(si.item_for_collection_cr_pr_price * si.item_for_collection_amount) " +
                    "    from (sale s join items_for_collection i on i.output_fk=s.output_id) si",
            nativeQuery = true
    )
    Double sumOfAllSales();

    // count of clients
    @Query (
            value = "select count(*) from client c",
            nativeQuery = true
    )
    Long countClients();

    // count of all debt clients
    @Query (
            value = "select count(*) " +
                    "    from client c join balance b on b.id = c.balance_id " +
                    "        where b.balance_amount < 0.0;",
            nativeQuery = true
    )
    Long countOfDebtClients();

    // sum of all the debt sum of clients
    @Query (
            value = "select sum(b.balance_amount) " +
                    "    from client c join balance b on b.id = c.balance_id " +
                    "        where b.balance_amount < 0.0;",
            nativeQuery = true
    )
    Double sumOfAllDebtSumOfClients();

    // sum of all the paid sum of sale
    @Query (
            value = "select sum(s.sale_whole_price - s.sale_debt_price) from sale s;",
            nativeQuery = true
    )
    Double sumOfAllPaidSumsOfSale();

    // clients bought products
    @Query (
            value = "select sum(oi.item_for_collection_amount) as sumAmount, sc.client_name as clientName, sc.client_id as clientId " +
                    "from (sale s join client c on s.client_id = c.id) sc " +
                    "        join (output o join items_for_collection ifc on o.id = ifc.output_fk) oi on sc.output_id=oi.output_fk " +
                    "group by sc.client_name, sc.client_id;",
            nativeQuery = true
    )
    List<ClientListBySumAmount> allClientsByBoughtProducts();

    // benefit by sold products
    @Query (
            value = "select sum(si.item_for_collection_cr_pr_price * si.item_for_collection_amount) - sum(si.item_for_collection_cr_ingr_price * si.item_for_collection_amount) " +
                    "from (sale s join items_for_collection i on i.output_fk=s.output_id) si",
            nativeQuery = true
    )
    Double benefitBySoldProducts();

    // debt clients list
    @Query(
            value = "select sum(b.balance_amount) " +
                    "    from employee e join balance b on b.id = e.balance_id " +
                    "        where b.balance_amount < 0.0;",
            nativeQuery = true
    )
    Double sumOfAllDebtSumEmployers();

    // client list by payments
    @Query(
            value = "select sum(s.sale_whole_price - s.sale_debt_price) as sumAmount, c.id as clientId, c.client_name as clientName " +
                    "    from sale s join client c on s.client_id = c.id " +
                    "    group by c.id;",
            nativeQuery = true
    )
    List<ClientListBySumAmount> clientListByPayments();

    // sold products list by amount
    @Query(
            value = "with bum as (select sum(ifc.item_for_collection_amount) as sumAmount, ifc.product_item_id as itemId " +
                    "    from output o join items_for_collection ifc on o.id = ifc.output_fk " +
                    "            where o.output_type='SALE' " +
                    "            group by ifc.product_item_id) " +
                    "select p.id as id, sum(bum.sumAmount) as sumAmount, p.product_name as ProductName " +
                    "    from product_item pi join bum on bum.itemId=pi.id join product p on pi.item_product_id = p.id " +
                    "    group by p.id, p.product_name;",
            nativeQuery = true
    )
    List<ProductListBySumAmount> productListByAmount();

    // benefits of input products production
    @Query(
            value = "select sum(i.input_amount) as amount, pi.item_product_id as productId, p.product_name as productName, sum(i.input_cr_pr_price * i.input_amount) as sumWholePrice, sum(i.input_cr_pr_ingr_price * i.input_amount) as sumRealPrice " +
                    "    from input i join product_item pi on i.product_item_id = pi.id " +
                    "    join product p on pi.item_product_id = p.id " +
                    "    where i.input_product_type='PRODUCT' " +
                    "    group by pi.item_product_id, p.product_name;",
            nativeQuery = true
    )
    List<ProductListAboutInput> productListAboutInput();

    @Query(
            value = "select sum(payment_amount) " +
                    "from payment where payment_status='INCOME';",
            nativeQuery = true
    )
    Double incomePayments();
    
    @Query(
            value = "select sum(payment_amount) from payment where payment_status='OUTCOME';",
            nativeQuery = true
    )
    Double outcomePayments();


    @Query(
            value = "select sum(outcome_amount) from outcome where user_id is null;",
            nativeQuery = true
    )
    Double sumOfOutcomeAmount();
}
