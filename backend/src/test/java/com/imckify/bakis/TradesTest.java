//package com.imckify.bakis;
//
//import com.imckify.bakis.models.Trades;
//import com.imckify.bakis.repos.TradesRepo;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class TradesTest {
//
//    @Autowired
//    private TradesRepo tradesRepo;
//
//    @Test
//    public void getTrade(){
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//
//        Trades trade = new Trades(2, "FB", "sell", 200.33, 199.99, 1, "EUR", dateFormat.format(new Date()), dateFormat.format(new Date()), 1);
//        Trades trade2 = new Trades(2, "AAPL", "buy", 134.33, 150, 2, "EUR", dateFormat.format(new Date()), dateFormat.format(new Date()), 1);
//        tradesRepo.save(trade);
//
//        tradesRepo.save(trade2);
//
//        tradesRepo.findById(new Integer(2))
//                .map(newTrade ->{
//                    Assert.assertEquals("AAPL",newTrade.getTicker());
//                    return true;
//                });
//
//    }
//}
