package it.polimi.ingsw.psp40.controller;

import it.polimi.ingsw.psp40.commons.Component;
import it.polimi.ingsw.psp40.commons.PhaseType;
import it.polimi.ingsw.psp40.controller.strategies.strategyBuild.StrategyBuild;
import it.polimi.ingsw.psp40.controller.strategies.strategyMove.StrategyMove;
import it.polimi.ingsw.psp40.controller.strategies.strategyWin.StrategyWin;
import it.polimi.ingsw.psp40.exceptions.SantoriniException;
import it.polimi.ingsw.psp40.model.Cell;
import it.polimi.ingsw.psp40.model.Player;
import it.polimi.ingsw.psp40.model.Worker;

import java.util.List;
import java.util.stream.Collectors;

public class Turn {
    private Player player;

    private Phase currentPhase;

    private Worker selectedWorker;

    private StrategyMove strategyMove;
    private StrategyWin strategyWin;
    private StrategyBuild strategyBuild;

    public Turn(Player player) {
        this.player = player;
        initializeTurn();
    }

    public void initializeTurn() {
        currentPhase = player.getCurrentCard().getInitialPhase();
        selectedWorker = null;
    }

    public void setStrategyBuild(StrategyBuild strategyBuild) {
        this.strategyBuild = strategyBuild;
    }

    public void setStrategyMove(StrategyMove strategyMove) {
        this.strategyMove = strategyMove;
    }

    public void setStrategyWin(StrategyWin strategyWin) {
        this.strategyWin = strategyWin;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Cell> getAvailableCellForMove() {
        return this.strategyMove.getAvailableCells(selectedWorker);
    }


    public void move(Cell cell) throws SantoriniException {
        this.strategyMove.move(selectedWorker, cell);
    }

    public List<Cell> getAvailableCellForBuild() {
        return this.strategyBuild.getBuildableCells(selectedWorker);
    }

    public void build(Component component, Cell cell) throws SantoriniException {
        this.strategyBuild.build(component, cell, selectedWorker);
    }

    public boolean checkWin() {
        return this.strategyWin.checkWin();
    }

    public void setSelectedWorker(Worker selectedWorker) {
        //aggiunger check che il worker appartenga al player
        this.selectedWorker = selectedWorker;
    }

    public boolean noAvailableCellForWorkers() {
        return player.getWorkers().stream().filter(worker -> strategyMove.getAvailableCells(worker).size() > 0).count() == 0;
    }

    public void updateCurrentPhaseFromType(PhaseType type) {
        Phase tempCurrentPhase = currentPhase.getNextPhases().stream().filter(phase -> phase.getType().equals(type)).findFirst().orElse(null);
        if (tempCurrentPhase == null) throw new RuntimeException("Cannot update CurrentPhase with a null phase");
        else currentPhase = tempCurrentPhase;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }
}