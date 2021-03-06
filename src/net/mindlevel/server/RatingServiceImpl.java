package net.mindlevel.server;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mindlevel.client.services.RatingService;
import net.mindlevel.shared.User;
//import com.yourdomain.projectname.client.User;
@SuppressWarnings("serial")
public class RatingServiceImpl extends DBConnector implements RatingService {

    private final static int upVoteValue = 10;
    private final static int upVoteCost = -1;
    private final static int downVoteValue = -10;
    private final static int downVoteCost = -10;

    @Override
    public int getVoteValue(String username, int pictureId) throws IllegalArgumentException{
        int value = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT score FROM rating "
                    + "WHERE username = ? AND picture_id = ?");
            ps.setString(1, username);
            ps.setInt(2, pictureId);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                value = rs.getInt("score");
            }
            rs.close();
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void setVoteValue(String token, int pictureId, boolean isUpVote) throws IllegalArgumentException {
        try {
            Connection conn = getConnection();
            User user = new UserServiceImpl().getUserFromToken(token);
            //Check if user has already voted
            PreparedStatement precheck1 = conn.prepareStatement("SELECT u.username, r.picture_id FROM user u "
                    + "LEFT JOIN rating r ON u.username = r.username "
                    + "WHERE u.username = ? AND r.picture_id = ?");
            precheck1.setString(1, user.getUsername());
            precheck1.setInt(2, pictureId);

            //Check if user has enough score to vote
            PreparedStatement precheck2 = conn.prepareStatement("SELECT username FROM user "
                    + "WHERE username = ? AND score > ?");
            precheck2.setString(1, user.getUsername());
            precheck2.setInt(2, isUpVote ? -1*upVoteCost : -1*downVoteCost);

            ResultSet rs1 = precheck1.executeQuery();
            ResultSet rs2 = precheck2.executeQuery();
            boolean hasVoted = rs1.first();
            boolean hasScore = rs2.first();
            if(!hasVoted && hasScore) {

                //Insert the rating to keep track of which votes that have been cast
                PreparedStatement ps = conn.prepareStatement("INSERT INTO rating "
                        + "(username, picture_id, score) values (?,?,?) "
                        + "on duplicate key update score=values(score)");
                ps.setString(1, user.getUsername());
                ps.setInt(2, pictureId);
                ps.setInt(3, isUpVote ? 1 : -1);
                ps.executeUpdate();
                ps.close();

                //Update the receivers score
                PreparedStatement ps2 = conn.prepareStatement("UPDATE user u "
                        + "INNER JOIN user_picture p ON u.username = p.username "
                        + "SET score = score + ? WHERE p.picture_id = ?");
                ps2.setInt(1, isUpVote ? upVoteValue : downVoteValue);
                ps2.setInt(2, pictureId);
                ps2.executeUpdate();
                ps2.close();

                //Update the givers score
                PreparedStatement ps3 = conn.prepareStatement("UPDATE user u "
                        + "SET score = score + ? WHERE u.username = ?");
                ps3.setInt(1, isUpVote ? upVoteCost : downVoteCost);
                ps3.setString(2, user.getUsername());
                ps3.executeUpdate();
                ps3.close();

                //Update the pictures score
                PreparedStatement ps4 = conn.prepareStatement("UPDATE picture "
                        + "SET score = score + ? WHERE id = ?");
                ps4.setInt(1, isUpVote ? 1 : -1);
                ps4.setInt(2, pictureId);
                ps4.executeUpdate();
                ps4.close();

                conn.close();
            } else if(!hasScore){
                throw new IllegalArgumentException("I'm afraid you've don't have enough score to cast that vote.");
            } else {
                throw new IllegalArgumentException("You've already voted on this picture");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getScore(int id) throws IllegalArgumentException {
        int score = 0;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(score) As vote_score "
                    + "FROM rating WHERE picture_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                score = rs.getInt("vote_score");
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return score;
    }

    @Override
    public int getVoteNumber(int id, boolean countUpVotes, boolean countDownVotes) throws IllegalArgumentException {
        int total = 0;
        try {
            Connection conn = getConnection();
            String constraint = "";

            if(countUpVotes && !countDownVotes) {
                constraint = "AND score > 0";
            } else if(!countUpVotes && countDownVotes) {
                constraint = "AND score < 0";
            } else if(!countUpVotes && !countDownVotes) {
                constraint = "AND score < 0 AND score > 0";
            }
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(score) AS vote_number "
                    + "FROM rating WHERE picture_id=? " + constraint);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                total = rs.getInt("vote_number");
            }
            ps.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void deleteRatings(int pictureId, String token) throws IllegalArgumentException {
        if(new TokenServiceImpl().validateAdminToken(token)) {
            Connection conn = getConnection();
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement("DELETE FROM rating WHERE picture_id=?");
                ps.setInt(1, pictureId);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not delete ratings.");
            }
        } else {
            throw new IllegalArgumentException("YOU don't seem to be admin. This was logged.");
        }
    }
}