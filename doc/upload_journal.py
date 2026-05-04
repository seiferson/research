import os
from datetime import datetime
from pathlib import Path

from dotenv import load_dotenv
from pymongo import MongoClient


load_dotenv()

now = datetime.now().astimezone()
today_file = Path("tmp") / f"{now.year}{now.strftime('%b').lower()}{now.day}.md"

doc = {
    "created": now.isoformat(timespec="seconds"),
    "updated": now.isoformat(timespec="seconds"),
    "author": os.environ["AUTHOR"],
    "content": today_file.read_text(encoding="utf-8"),
}

client = MongoClient(os.environ["MONGODB_ATLAS_URI"])
result = client[os.getenv("MONGODB_ATLAS_DB", "research_data")]["journal"].insert_one(doc)
print(result.inserted_id)
client.close()
