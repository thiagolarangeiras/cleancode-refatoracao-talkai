package com.satc.integrador.ai.planoestudo;

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
    public ResponseEntity<PlanoEstudo> generateNewPlan() {
        return ResponseEntity.ok(planoEstudoService.handleNewPlan());
    }

    @GetMapping("today")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudo> get() {
        return ResponseEntity.ok(planoEstudoService.getCurrent());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudo> getByid(@PathVariable Integer id) {
        return ResponseEntity.ok(planoEstudoService.getById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlanoEstudoListaGetDto> getAll() {
        return ResponseEntity.ok(planoEstudoService.getAll());
    }

    @PostMapping("{id}/finalizar-exercicio/{id-exercicio}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseEntity> finalizarExercicio(@PathVariable Integer id,
                                                      @PathVariable("id-exercicio") Integer idExercicio) {
        planoEstudoService.finalizarExercicio(id, idExercicio);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/finalizar-plano-diario")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> finalizarPlanoDiario(@PathVariable Integer id) {
        return ResponseEntity.ok(planoEstudoService.finalizarPlanoDiario(id));
    }
}