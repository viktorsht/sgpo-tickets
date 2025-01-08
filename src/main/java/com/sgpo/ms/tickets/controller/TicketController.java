package com.sgpo.ms.tickets.controller;

import com.sgpo.ms.tickets.config.ApiClient;
import com.sgpo.ms.tickets.dto.TravelRoutes;
import com.sgpo.ms.tickets.dto.UserDataResponse;
import com.sgpo.ms.tickets.dto.TicketRequestDto;
import com.sgpo.ms.tickets.dto.TicketResponseDto;
import com.sgpo.ms.tickets.entities.Ticket;
import com.sgpo.ms.tickets.repository.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final ApiClient apiClient;

    public TicketController(
            TicketRepository ticketRepository,
            ApiClient apiClient) {
        this.ticketRepository = ticketRepository;
        this.apiClient = apiClient;
    }

    @GetMapping
    public List<TicketResponseDto> getAllTicketsWithDetails(JwtAuthenticationToken token) {
        // Busca todos os tickets
        var list = ticketRepository.findAll();

        // Mapeia cada ticket para um TicketResponseDto
        List<TicketResponseDto> responseDtoList = list.stream()
                .map(ticket -> {
                    // Obtém os dados do usuário a partir da API externa
                    UserDataResponse userData = apiClient.getUserData(token.getToken().getTokenValue());

                    // Obtém os dados da rota a partir da API externa
                    TravelRoutes routeData = apiClient.getTravelRoutes(
                            token.getToken().getTokenValue(),
                            ticket.getTravelRouteId().toString()
                    );

                    // Constrói o DTO para o ticket
                    return new TicketResponseDto(
                            ticket.getId(),
                            userData,        // Dados do usuário
                            routeData,       // Dados da rota
                            ticket.getCardNumber(),
                            ticket.getNumberOfTickets(),
                            ticket.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        // Retorna a lista de DTOs
        return responseDtoList;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<TicketResponseDto>> getTicketByUser(JwtAuthenticationToken token, @PathVariable UUID userId) {
        // Obtém todos os tickets
        var ticketList = ticketRepository.findAll();

        // Obtém os dados do usuário a partir do serviço externo
        UserDataResponse userData = apiClient.getUserData(token.getToken().getTokenValue());

        // Filtra os tickets pelo ID do usuário
        List<TicketResponseDto> responseDtoList = ticketList.stream()
                .filter(ticket -> ticket.getUserId().equals(userId)) // Filtra pelo usuário
                .map(ticket -> {
                    // Obtém os dados da rota para cada ticket
                    TravelRoutes routeData = apiClient.getTravelRoutes(
                            token.getToken().getTokenValue(),
                            ticket.getTravelRouteId().toString()
                    );

                    // Constrói o TicketResponseDto
                    return new TicketResponseDto(
                            ticket.getId(),
                            userData,
                            routeData,
                            ticket.getCardNumber(),
                            ticket.getNumberOfTickets(),
                            ticket.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }


    @PostMapping
    public ResponseEntity<Void> createTicket(JwtAuthenticationToken token, @RequestBody TicketRequestDto ticketRequestDto) {
        UserDataResponse userData = apiClient.getUserData(token.getToken().getTokenValue());

        TravelRoutes routeData = apiClient.getTravelRoutes(token.getToken().getTokenValue(), ticketRequestDto.travelRouteId().toString());

        Ticket ticket = new Ticket();
        ticket.setUserId(userData.id());
        ticket.setTravelRouteId(routeData.id());
        ticket.setCardNumber(ticketRequestDto.cardNumber());
        ticket.setNumberOfTickets(ticketRequestDto.numberOfTickets());
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);
        System.out.println(savedTicket.toString());

        return ResponseEntity.ok().build();
    }


    // PUT: Atualiza um ticket existente
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            ticket.setUserId(ticketDetails.getUserId());
            ticket.setTravelRouteId(ticketDetails.getTravelRouteId());
            ticket.setCardNumber(ticketDetails.getCardNumber());
            ticket.setNumberOfTickets(ticketDetails.getNumberOfTickets());
            Ticket updatedTicket = ticketRepository.save(ticket);
            return ResponseEntity.ok(updatedTicket);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: Remove um ticket pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
