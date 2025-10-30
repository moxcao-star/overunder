//package com.example.overunder.scheduler;
//
//import com.example.overunder.model.Game;
//import com.example.overunder.gamerunner.Dealer;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//public class CustomScheduler {
//    private final Scheduler scheduler;
//    public void scheduleNext(Game game, int seconds) {
//        try {
//            JobKey jobKey = new JobKey(game.getGameId());
//            if(scheduler.checkExists(jobKey)) {
//                scheduler.deleteJob(jobKey);
//            }
//            JobDetail jobDetail = JobBuilder.newJob(Dealer.class)
//                    .withIdentity(gxame.getGameId())
//                    .usingJobData("gameId", game.getGameId())
//                    .build();
//            Trigger trigger = TriggerBuilder.newTrigger()
//                    .startAt(DateBuilder.futureDate(seconds, DateBuilder.IntervalUnit.SECOND))
//                    .build();
//
//            scheduler.scheduleJob(jobDetail, trigger);
//        } catch (SchedulerException e) {
//            log.error("Error scheduling next phase={} for game {}", game.getNextStatus().toString(), game.getGameId(), e);
//        }
//    }
//    public boolean isNotExists(JobKey jobKey) {
//        try {
//            return !scheduler.checkExists(jobKey);
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public void deleteJob(JobKey jobKey) {
//        try {
//            scheduler.deleteJob(jobKey);
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
