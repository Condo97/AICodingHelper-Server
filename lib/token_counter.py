import sys
import tiktoken

def count_tokens(model_name, input_string):
    enc = tiktoken.encoding_for_model(model_name)
    tokens = enc.encode(input_string)
    return len(tokens)

if __name__ == "__main__":
    try:
        model_name = sys.stdin.readline().strip()
        input_string = sys.stdin.read().strip()

        if not model_name or not input_string:  # Check if input is missing
            raise ValueError("Model name or input string is missing")

        token_count = count_tokens(model_name, input_string)
        print(token_count)
        sys.stdout.flush()
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)  # Use a non-zero exit code to indicate error