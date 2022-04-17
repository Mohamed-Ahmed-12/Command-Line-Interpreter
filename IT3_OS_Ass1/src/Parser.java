public class Parser {

    String commandName;
    String[] args = new String[10];

    public boolean parse(String input) {
        String[] arg = {"ls -r", "ls", "pwd", "cd", "rmdir", "rm", "echo", "touch", "cp -r", "cp", "cat", "mkdir"};
        int x = 0, d = 0, m = 0, z = 0;
        for (int i = 0; i < 12; i++) {
            if (input.contains(arg[i])) {
                x = 1;
                d = i;
                break;
            }
        }
        m = arg[d].length();
        if (0 <= d && d < 3 && m != input.length()) {
            return false;
        }
        if (m < input.length()) {
            if (input.charAt(m) != ' ') {
                return false;
            }
        }
        if (!input.substring(0, m).equals(arg[d]) || x == 0) {
            return false;
        } else {
            if(input.length()==m && d>3)
                return false;
            commandName = arg[d];
            if (d == 3 && input.length() > 2) {
                args[0] = input.substring(3);
            }
            if (3 < d && d < 8) {
                args[0] = input.substring(m + 1);
            }
            if (d == 8 || d == 9) {
                for (int i = m + 1; i < input.length(); i++) {
                    if (input.charAt(i) == ' ') {
                        z = i;
                        break;
                    }
                }
                if (z == 0) {
                    return false;
                }
                args[0] = input.substring(m + 1, z);
                args[1] = input.substring(z + 1);
            }
            if (d == 10) {
                for (int i = m; i < input.length(); i++) {
                    if (input.charAt(i) == ' ') {
                        z = i;
                        break;
                    }
                }
                if (z != 0) {
                    args[0] = input.substring(m, z);
                    args[1] = input.substring(z + 1);
                } else {
                    args[0] = input.substring(m + 1);
                }
            }
            if (d == 11) {
                int y = 0, zz = m + 1;
                for (int i = m + 2; i < input.length(); i++) {
                    if (input.charAt(i) == ' ' || i == input.length() - 1) {
                        if (i == input.length() - 1) {
                            i++;
                        }
                        args[y] = input.substring(zz, i);
                        zz = i + 1;
                        y++;
                    }
                }
            }

            return true;
        }
    }

    public String getCommandName() {

        return commandName;
    }

    public String[] getArgs() {

        return args;
    }
}