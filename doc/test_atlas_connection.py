import os

from dotenv import load_dotenv
from pymongo import MongoClient


load_dotenv()

uri = os.getenv("MONGODB_ATLAS_URI")

if not uri:
    raise SystemExit("MONGODB_ATLAS_URI is not set")

client = MongoClient(uri, serverSelectionTimeoutMS=5000)

try:
    client.admin.command("ping")
    print("Connected to MongoDB Atlas.")
except Exception as exc:
    raise SystemExit(f"Connection failed: {exc}") from exc
finally:
    client.close()
