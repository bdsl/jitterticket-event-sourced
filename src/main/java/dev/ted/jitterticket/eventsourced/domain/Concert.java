package dev.ted.jitterticket.eventsourced.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Concert extends EventSourcedAggregate<ConcertEvent, Id> {

    private String artist;
    private int ticketPrice;
    private LocalDateTime showDateTime;
    private LocalTime doorsTime;
    private int capacity;
    private int maxTicketsPerPurchase;

    private Concert(List<ConcertEvent> concertEvents) {
        concertEvents.forEach(this::apply);
    }

    public static Concert schedule(String artist,
                                   int ticketPrice,
                                   LocalDateTime showDateTime,
                                   LocalTime doorsTime,
                                   int capacity,
                                   int maxTicketsPerPurchase) {
        return new Concert(artist, ticketPrice, showDateTime, doorsTime, capacity, maxTicketsPerPurchase);
    }

    public static Concert reconstitute(List<ConcertEvent> concertEvents) {
        return new Concert(concertEvents);
    }

    private Concert(String artist, int ticketPrice, LocalDateTime showDateTime, LocalTime doorsTime, int capacity, int maxTicketsPerPurchase) {
        ConcertScheduled concertScheduled = new ConcertScheduled(
                artist, ticketPrice, showDateTime, doorsTime, capacity, maxTicketsPerPurchase
        );
        enqueue(concertScheduled);
    }

    @Override
    protected void apply(ConcertEvent concertEvent) {
        switch (concertEvent) {
            case ConcertScheduled(
                    String artist,
                    int ticketPrice,
                    LocalDateTime showDateTime,
                    LocalTime doorsTime,
                    int capacity,
                    int maxTicketsPerPurchase
            ) -> {
                this.artist = artist;
                this.ticketPrice = ticketPrice;
                this.showDateTime = showDateTime;
                this.doorsTime = doorsTime;
                this.capacity = capacity;
                this.maxTicketsPerPurchase = maxTicketsPerPurchase;
            }

            case ConcertRescheduled(
                    LocalDateTime newShowDateTime,
                    LocalTime newDoorsTime
            ) -> {
                this.showDateTime = newShowDateTime;
                this.doorsTime = newDoorsTime;
            }
        }
    }

    public int ticketPrice() {
        return ticketPrice;
    }

    public LocalDateTime showDateTime() {
        return showDateTime;
    }

    public LocalTime doorsTime() {
        return doorsTime;
    }

    public int capacity() {
        return capacity;
    }

    public int maxTicketsPerPurchase() {
        return maxTicketsPerPurchase;
    }

    public String artist() {
        return artist;
    }

    public void rescheduleTo(LocalDateTime newShowDateTime, LocalTime newDoorsTime) {
        ConcertRescheduled concertRescheduled = new ConcertRescheduled(newShowDateTime, newDoorsTime);
        enqueue(concertRescheduled);
    }

}
