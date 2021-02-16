package com.hb.mcfdebugger.commandHelpers;
import com.hb.mcfdebugger.DebugThread;
import com.hb.mcfdebugger.McfDebugger;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ObjectiveArgumentType;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.ServerScoreboard;

import java.util.*;

public class ReadScoreboard {
    public static void parseAllScoreboard()throws Error{

        Collection<ScoreboardObjective> objectiveList = McfDebugger.mcServer.getScoreboard().getObjectives();
        List<SendObjective> sendObjectiveList=new LinkedList<>();
        for(ScoreboardObjective nowObjective:objectiveList){
            SendObjective sendObjective=new SendObjective(nowObjective);
            sendObjectiveList.add(sendObjective);
        }
        DebugThread.sendObjMsgToDebugger(sendObjectiveList,"scoreboardList");
    }
    public static void parseScoreboardByEntity(CommandContext commandContext) throws CommandSyntaxException {
        ServerScoreboard objectives = McfDebugger.mcServer.getScoreboard();
        Collection<String> entities=ScoreHolderArgumentType.getScoreboardScoreHolders(commandContext, "targets");
        entities.forEach((String entity)->{
            Map<ScoreboardObjective, ScoreboardPlayerScore> playerScores=objectives.getPlayerObjectives(entity);
            PlayerScorePair playerScorePair=new PlayerScorePair(entity,playerScores);
            DebugThread.sendObjMsgToDebugger(playerScorePair,"getScoresResultByEntity");

        });
    }
    public static void parseScoreboardByObjective(CommandContext commandContext) throws CommandSyntaxException {
        ServerScoreboard objectives = McfDebugger.mcServer.getScoreboard();
        ScoreboardObjective objective= ObjectiveArgumentType.getObjective(commandContext,"objective");
        Collection<ScoreboardPlayerScore> scores=objectives.getAllPlayerScores(objective);
        ObjectiveScorePair objectiveScorePair = new ObjectiveScorePair(objective.getName(),scores);
        DebugThread.sendObjMsgToDebugger(objectiveScorePair,"getScoresResultByObjective");

    }

    public static class SendObjective{
        public Map<String,Integer> playerScores=new HashMap<String, Integer>();
        public String name;
        public String criterion;
        public String disPlayName;
        public SendObjective(ScoreboardObjective mcObjective){
            this.name=mcObjective.getName();
            this.criterion=mcObjective.getCriterion().getName();
            this.disPlayName=mcObjective.getDisplayName().getString();
            for(ScoreboardPlayerScore playerScore:mcObjective.getScoreboard().getAllPlayerScores(mcObjective)){
                this.playerScores.put(playerScore.getPlayerName(),playerScore.getScore());
            }
        }
    }
    public static class PlayerScorePair{
        public String playerName;
        public Map<String,Integer> playerScore=new LinkedHashMap<>();
        public PlayerScorePair(String playerName,Map<ScoreboardObjective, ScoreboardPlayerScore> playerScore){
            this.playerName=playerName;
            playerScore.forEach((ScoreboardObjective objective,ScoreboardPlayerScore score)->{
                this.playerScore.put(objective.getName(),score.getScore());
            });
        }
    }
    public static class ObjectiveScorePair{
        public String objectiveName;
        public Map<String,Integer> objectiveScore=new LinkedHashMap<>();
        public ObjectiveScorePair(String objectiveName,Collection<ScoreboardPlayerScore> playerScore){
            this.objectiveName=objectiveName;
            playerScore.forEach((ScoreboardPlayerScore score)->{
                this.objectiveScore.put(score.getPlayerName(),score.getScore());
            });
        }
    }

}
