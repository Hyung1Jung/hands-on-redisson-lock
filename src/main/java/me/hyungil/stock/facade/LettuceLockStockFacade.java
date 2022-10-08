package me.hyungil.stock.facade;

import lombok.RequiredArgsConstructor;
import me.hyungil.stock.repository.RedisLockRepository;
import me.hyungil.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {

    private RedisLockRepository redisLockRepository;
    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }
        try {
            stockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unLock(key);
        }
    }
}