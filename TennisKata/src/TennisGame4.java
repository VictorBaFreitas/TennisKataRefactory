public class TennisGame4 implements TennisGame {

    private int serverScore;
    private int receiverScore;
    private String server;
    private String receiver;

    public TennisGame4(String player1, String player2) {
        this.server = player1;
        this.receiver = player2;
    }

    @Override
    public void wonPoint(String playerName) {
        if (server.equals(playerName))
            this.serverScore += 1;
        else
            this.receiverScore += 1;
    }

    @Override
    public String getScore() {
        TennisResult result = new Deuce(
                new GameServer(
                        new GameReceiver(
                                new AdvantageServer(
                                        new AdvantageReceiver(
                                                new DefaultResult()))))).getResult();
        return result.format();
    }

    boolean receiverHasAdvantage() {
        return receiverScore >= 4 && (receiverScore - serverScore) == 1;
    }

    boolean serverHasAdvantage() {
        return serverScore >= 4 && (serverScore - receiverScore) == 1;
    }

    boolean receiverHasWon() {
        return receiverScore >= 4 && (receiverScore - serverScore) >= 2;
    }

    boolean serverHasWon() {
        return serverScore >= 4 && (serverScore - receiverScore) >= 2;
    }

    boolean isDeuce() {
        return serverScore >= 3 && receiverScore >= 3 && (serverScore == receiverScore);
    }

    class TennisResult {
        String serverScore;
        String receiverScore;

        TennisResult(String serverScore, String receiverScore) {
            this.serverScore = serverScore;
            this.receiverScore = receiverScore;
        }

        String format() {
            if ("".equals(this.receiverScore))
                return this.serverScore;
            if (serverScore.equals(receiverScore))
                return serverScore + "-All";
            return this.serverScore + "-" + this.receiverScore;
        }
    }

    interface ResultProvider {
        TennisResult getResult();
    }

    class Deuce implements ResultProvider {
        private final ResultProvider nextResult;

        public Deuce(ResultProvider nextResult) {
            this.nextResult = nextResult;
        }

        @Override
        public TennisResult getResult() {
            if (isDeuce())
                return new TennisResult("Deuce", "");
            return this.nextResult.getResult();
        }
    }

    class GameServer implements ResultProvider {
        private final ResultProvider nextResult;

        public GameServer(ResultProvider nextResult) {
            this.nextResult = nextResult;
        }

        @Override
        public TennisResult getResult() {
            if (serverHasWon())
                return new TennisResult("Win for " + server, "");
            return this.nextResult.getResult();
        }
    }

    class GameReceiver implements ResultProvider {
        private final ResultProvider nextResult;

        public GameReceiver(ResultProvider nextResult) {
            this.nextResult = nextResult;
        }

        @Override
        public TennisResult getResult() {
            if (receiverHasWon())
                return new TennisResult("Win for " + receiver, "");
            return this.nextResult.getResult();
        }
    }

    class AdvantageServer implements ResultProvider {
        private final ResultProvider nextResult;

        public AdvantageServer(ResultProvider nextResult) {
            this.nextResult = nextResult;
        }

        @Override
        public TennisResult getResult() {
            if (serverHasAdvantage())
                return new TennisResult("Advantage " + server, "");
            return this.nextResult.getResult();
        }
    }

    class AdvantageReceiver implements ResultProvider {

        private final ResultProvider nextResult;

        public AdvantageReceiver(ResultProvider nextResult) {
            this.nextResult = nextResult;
        }

        @Override
        public TennisResult getResult() {
            if (receiverHasAdvantage())
                return new TennisResult("Advantage " + receiver, "");
            return this.nextResult.getResult();
        }
    }

    class DefaultResult implements ResultProvider {

        private static final String[] scores = {"Love", "Fifteen", "Thirty", "Forty"};

        @Override
        public TennisResult getResult() {
            return new TennisResult(scores[serverScore], scores[receiverScore]);
        }
    }
}