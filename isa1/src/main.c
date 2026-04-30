#include <stdarg.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>



typedef struct {
    unsigned short words[65536];
} memory_t;

typedef struct {
    unsigned short registers[8];
    memory_t memory;
} cpu_t;

void move(cpu_t *cpu, const char *dest, const char *src)
{
    int dest_register;
    unsigned short value;

    if (dest == NULL || src == NULL) {
        return;
    }

    if (dest[0] != 'r' || dest[1] < '0' || dest[1] > '7' || dest[2] != '\0') {
        return;
    }

    dest_register = dest[1] - '0';

    if (src[0] == '#') {
        value = (unsigned short) strtoul(src + 1, NULL, 0);
    } else if (src[0] == 'r' && src[1] >= '0' && src[1] <= '7' && src[2] == '\0') {
        value = cpu->registers[src[1] - '0'];
    } else {
        return;
    }

    cpu->registers[dest_register] = value;
}



char *split_token(char *value)
{
    return strtok(value, " \r\n");
}



int main(int argc, char **argv)
{
    if (argc > 1) {
        cpu_t cpu = {0};
        FILE *file;
        char line[1024];
        char line_value[6];
        char split_line[1024];
        char *mnemonic;
        int line_number = 1;

        log("");
        log("file::read::name [")
        log, argv[1]);

        file = fopen(argv[1], "r");
        if (file == NULL) {
            log_message("file::read::errr::fopen %s", argv[1]);
            return 1;
        }

        log_message("file::read::star");
        log_message("-------------------------------------------------");
        log_message("");

        while (fgets(line, sizeof(line), file) != NULL) {
            left_pad_line_number(line_number, line_value, sizeof(line_value));
            log_message("file::read::%s:: %s", line_value, line);
            strcpy(split_line, line);
            mnemonic = split_token(split_line);
            line_number++;
        }

        fclose(file);
        log_message("file::read::     :: EOF");
    }

    return 0;
}
