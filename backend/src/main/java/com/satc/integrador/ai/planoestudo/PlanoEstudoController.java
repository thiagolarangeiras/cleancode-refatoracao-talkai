package com.satc.integrador.ai.planoestudo;

import com.satc.integrador.ai.planoestudo.dto.PlanoEstudoGetDto;
import com.satc.integrador.ai.planoestudo.dto.PlanoEstudoListaGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plano-estudo")
public class PlanoEstudoController {

    @Autowired
    private PlanoEstudoService planoEstudoService;

    @PostMapping("generate-new-plan")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudoGetDto> generateNewPlan() {
        return ResponseEntity.ok(planoEstudoService.generateNewPlan());
    }

    @GetMapping("today")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudoGetDto> get() {
        return ResponseEntity.ok(planoEstudoService.getCurrent());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudoGetDto> getid(@PathVariable Integer id) {
        return ResponseEntity.ok(planoEstudoService.getId(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudoListaGetDto> getAll(@RequestParam Integer page,
                                                         @RequestParam Integer count) {
        return ResponseEntity.ok(planoEstudoService.getAll(page, count));
    }

    @PostMapping("{id}/finalizar-exercicio/{id-exercicio}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> finalizarExercicio(@PathVariable Integer id,
                                                      @PathVariable("id-exercicio") Integer idExercicio) {
        return ResponseEntity.ok(planoEstudoService.finalizarExercicio(id, idExercicio));
    }

    @PostMapping("{id}/finalizar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> finalizarPlanoDiario(@PathVariable Integer id) {
        return ResponseEntity.ok(planoEstudoService.finalizarPlanoDiario(id));
    }
}