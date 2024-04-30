package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.model.Game;
import org.example.model.Hand;
import org.example.service.BlackJackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blackjack")
@CrossOrigin
public class BlackJackController {

    @Autowired
    BlackJackService blackJackService;


    @GetMapping("/home")
    public ResponseEntity<?> getHomePage(HttpServletRequest request){
        if(request.getSession().getAttribute("game") != null){
            request.getSession().removeAttribute("game");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("BlackJack HomePage");
    }
    @GetMapping("/newgame")
    public ResponseEntity<?> startNewGame(HttpServletRequest request){
        //If no game attribute in session set it to gameinprogress
        if(request.getSession().getAttribute("game") == null){
            request.getSession().setAttribute("game", "gameInProgress");
        }

        blackJackService.startGame();

        Hand dealerHand = blackJackService.getHand("dealer");
        Hand playerHand = blackJackService.getHand("player");
        String result = null;

        if(blackJackService.checkIfBj(playerHand)){
            result = blackJackService.checkResult();
        }

        Game gameStatus = new Game(dealerHand, playerHand, true, result);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameStatus);
    }

    @PostMapping("/gameinprogress/hit")
    public ResponseEntity<?> playerHits( HttpServletRequest request){

        if(request.getSession().getAttribute("game") != null){
            blackJackService.playerHit();

            Hand playerHand = blackJackService.getHand("player");
            Hand dealerHand = blackJackService.getHand("dealer");
            boolean isPlayerBusted = blackJackService.isBust(playerHand);
            String result = null;

            if(blackJackService.checkIfBj(playerHand)){
                result = blackJackService.checkResult();
            }

            if(isPlayerBusted){
                result = blackJackService.checkResult();
            }

            Game gameStatus = new Game(dealerHand, playerHand, true, result);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(gameStatus);
        } else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No game in progress");
        }
    }

    @PostMapping("gameinprogress/stand")
    public ResponseEntity<?> playerStands(HttpServletRequest request){

        if(request.getSession().getAttribute("game") != null){
            blackJackService.playerStand();

            Hand dealerHand = blackJackService.getHand("dealer");
            Hand playerHand = blackJackService.getHand("player");
            String result = blackJackService.checkResult();

            Game gameStatus = new Game(dealerHand, playerHand, result);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(gameStatus);

        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No game in progress");
        }
    }
}
