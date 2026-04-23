#include <stdarg.h>
#include <stdio.h>

const char *emulator_tag = "[v0.01][fly]";

void log_message(const char *message, ...)
{
    va_list args;

    printf("%s::", emulator_tag);

    va_start(args, message);
    vprintf(message, args);
    va_end(args);

    printf("\n");
}

int main(int argc, char **argv)
{
    if (argc > 1) {
        log_message("evaluating file %s", argv[1]);
    }

    return 0;
}
