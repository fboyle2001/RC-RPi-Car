# Maintains the versioning

from argparse import ArgumentParser
from bs4 import BeautifulSoup as HTMLEditor
from zipfile import ZipFile
import os
import shutil
import webbrowser
import re

def package(input_file, version):
    output_file = f"rpi-rc-controller-r{version}.zip"

    if os.path.isdir("temp"):
        shutil.rmtree("temp")

    os.makedirs("temp")

    input_zip = ZipFile(input_file, "r")
    input_zip.extractall("temp")
    input_zip.close()

    path_to_index = "./temp/controller/index.html"
    index_page = open(path_to_index, "r")
    html = HTMLEditor(index_page, "html.parser")

    for line in html.findAll("script", {"src": re.compile("^(?!.*jquery)")}):
        src = line["src"]

        if "?" in src:
            src = src.split("?")[0]

        src += "?v=" + version

        new_tag = html.new_tag("script")
        new_tag["src"] = src

        if "cache.js" in src:
            new_tag["data-version"] = version

        line.replaceWith(new_tag)

    css = html.find("link")
    new_css = html.new_tag("link")

    if "?" in css["href"]:
        new_css["href"] = css["href"].split("?")[0] + "?v=" + version
    else:
        new_css["href"] = css["href"] + "?v=" + version

    new_css["rel"] = "stylesheet"
    new_css["type"] = "text/css"
    css.replaceWith(new_css)

    index_page.close()

    with open("./temp/controller/index.html", "w") as index_page:
        index_page.write(html.prettify())

    shutil.make_archive("rpi-rc-controller-r" + version, "zip", "./temp/")
    shutil.rmtree("temp")

__name__ = "test"

if __name__ == "test":
    package("controller.zip", "0.2.0")

if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-i", "--input", dest="input_file", help="Input file")
    parser.add_argument("-v", "--version", dest="version", help="Version")

    args = parser.parse_args()

    if args.input_file == None:
        print("Input file cannot be empty.")
        exit()
    if args.input_file.split(".")[len(args.input_file.split(".")) - 1] != "zip":
        print("Input file must be a zip file.")
        exit()
    if os.path.isfile(args.input_file) == False:
        print("Input file must exist.")
        exit()

    if args.version == None:
        print("Version must be set.")
        exit()
