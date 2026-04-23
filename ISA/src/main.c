#include <stdarg.h>
#include <stdio.h>
#include <string.h>

const char *emulator_tag = "[v0.01][fly]cpuemu";

void log_message(const char *message, ...)
{
    va_list args;

    printf("%s::", emulator_tag);

    va_start(args, message);
    vprintf(message, args);
    va_end(args);

    printf("\n");
}

char *split_token(char *value)
{
    return strtok(value, " \r\n");
}

int main(int argc, char **argv)
{
    if (argc > 1) {
        FILE *file;
        char line[1024];
        char split_line[1024];
        char *mnemonic;
        int line_number = 1;

        log_message("");
        log_message("file::read::name [%s]", argv[1]);

        file = fopen(argv[1], "r");
        if (file == NULL) {
            log_message("file::read::errr::fopen %s", argv[1]);
            return 1;
        }

        log_message("file::read::star");
        log_message("-------------------------------------------------");
        log_message("");

        while (fgets(line, sizeof(line), file) != NULL) {
            log_message("file::read::%d   %s", line_number, line);
            strcpy(split_line, line);
            mnemonic = split_token(split_line);

            if (mnemonic != NULL) {
                log_message("%d mnemonic: %s", line_number, mnemonic);
            }

            line_number++;
        }

        fclose(file);
        log_message("file::read::EOF");
    }

    return 0;
}
