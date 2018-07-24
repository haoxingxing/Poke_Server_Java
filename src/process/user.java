package process;

import io.file;
import io.md5;

import java.io.IOException;

class user {
    static final int logpar = 2;
    static final int regpar = 2;

    static boolean login(String Username, String Password) {
        try {
            return file.read(Username + "/" + "md5").equals(md5.md5_encode(Password));
        } catch (IOException e) {
            return false;
        }
    }

    static boolean register(String Username, String Password) {
        if (file.isexists(Username + "/" + "md5")) {
            return false;
        } else {
            try {
                file.write(Username + "/" + "md5", md5.md5_encode(Password));
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }
}
