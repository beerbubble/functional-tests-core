package functional.tests.core.Gestures;

import functional.tests.core.Appium.Client;
import functional.tests.core.Element.Element;
import functional.tests.core.Enums.PlatformType;
import functional.tests.core.Find.Find;
import functional.tests.core.Find.Locators;
import functional.tests.core.Find.Wait;
import functional.tests.core.Log.Log;
import functional.tests.core.Settings.Settings;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;

import java.util.HashMap;

public class Gestures {

    private final Client client;
    private final Wait wait;
    private final Find find;

    public Gestures(Client client, Find find, Wait wait) {
        this.client = client;
        this.wait = wait;
        this.find = find;
    }

    public void tap(MobileElement element, int fingers, int duration, int waitAfterTap) {
        element.tap(fingers, duration);
        if (waitAfterTap > 0) {
            this.wait.sleep(waitAfterTap);
        }
        Log.info("Tap " + Element.getDescription(element));
    }

    public void tap(MobileElement element, int fingers) {
        tap(element, fingers, Settings.defaultTapDuration, Settings.defaultTapDuration);
    }

    public void tap(MobileElement element) {
        tap(element, 1, Settings.defaultTapDuration, Settings.defaultTapDuration);
    }

    public void swipe(SwipeElementDirection direction, int duration, int waitAfterSwipe) {

        // In iOS swipe with duration < 0.5 seconds is not possible
        if (Settings.platform == PlatformType.iOS) {
            if (duration < 500) {
                duration = 500;
            }
        }

        Dimension dimensions = this.client.driver.manage().window().getSize();
        int width = dimensions.width;
        int height = dimensions.height;
        int centerY = height / 2;
        int centerX = width / 2;

        int initialX = centerX;
        int initialY = centerY;
        int finalX = centerX;
        int finalY = centerY;

        int offset = (int) (height * 0.25D);

        if (direction == SwipeElementDirection.DOWN) {
            initialX = centerX;
            initialY = centerY + offset;
            finalX = centerX;
            finalY = centerY - offset;
        }
        if (direction == SwipeElementDirection.UP) {
            initialX = centerX;
            initialY = centerY - offset;
            finalX = centerX;
            finalY = centerY + offset;
        }
        if (direction == SwipeElementDirection.LEFT) {
            initialX = centerX + offset;
            initialY = centerY;
            finalX = centerX - offset;
            finalY = centerY;
        }
        if (direction == SwipeElementDirection.RIGHT) {
            initialX = centerX - offset;
            initialY = centerY;
            finalX = centerX + offset;
            finalY = centerY;
        }

        try {
            this.client.driver.swipe(initialX, initialY, finalX, finalY, duration);
            Log.info("Swipe " + direction + " with " + duration + " duration.");
            if (waitAfterSwipe > 0) {
                this.wait.sleep(waitAfterSwipe);
            }
        } catch (Exception e) {
            if ((Settings.platform == PlatformType.Andorid) && (Settings.platformVersion.contains("4.2"))) {
                Log.info("Known issue: Swipe works on Api17, but error is thrown.");
            } else {
                String error = "Swipe " + direction + " with " + duration + " duration failed.";
                Log.error(error);
                Assert.fail(error);
            }
        }
    }

    private void swipeFromCorner(SwipeElementDirection direction, int duration, int waitAfterSwipe) {

        int initialX = 0, initialY = 0, finalX = 0, finalY = 0;

        // In iOS swipe with duration < 0.5 seconds is not possible
        if (Settings.platform == PlatformType.iOS) {
            if (duration < 500) {
                duration = 500;
            }
        }

        Dimension dimensions = this.client.driver.manage().window().getSize();
        int width = dimensions.width;
        int height = dimensions.height;
        int centerY = height / 2;
        int centerX = width / 2;

        int left = 3;
        int top = 7;
        int right = width - 3;
        int bottom = height - 7;

        int offset = (int) (height * 0.5D);

        if (direction == SwipeElementDirection.DOWN) {
            initialX = centerX;
            initialY = top;
            finalX = centerX;
            finalY = top + offset;
        }
        if (direction == SwipeElementDirection.UP) {
            initialX = centerX;
            initialY = bottom;
            finalX = centerX;
            finalY = bottom - offset;
        }
        if (direction == SwipeElementDirection.LEFT) {
            initialX = right;
            initialY = centerY;
            finalX = right - offset;
            finalY = centerY;
        }
        if (direction == SwipeElementDirection.RIGHT) {
            initialX = left;
            initialY = centerY;
            finalX = left + offset;
            finalY = centerY;
        }

        try {
            this.client.driver.swipe(initialX, initialY, finalX, finalY, duration);
            Log.info("Swipe " + direction + " with " + duration + " duration.");
            if (waitAfterSwipe > 0) {
                this.wait.sleep(waitAfterSwipe);
            }
        } catch (Exception e) {
            if ((Settings.platform == PlatformType.Andorid) && (Settings.platformVersion.contains("4.2"))) {
                Log.info("Known issue: Swipe works on Api17, but error is thrown.");
            } else {
                String error = "Swipe " + direction + " with " + duration + " duration failed.";
                Log.error(error);
                Assert.fail(error);
            }
        }
    }

    public void swipe(SwipeElementDirection direction, int duration) {
        swipe(direction, duration, 0);
    }

    public void swipe(MobileElement element, String direction, int duration) {
        swipeInElement(element, direction, duration);
    }

    public void swipeFromCorner(SwipeElementDirection direction, int duration) {
        swipeFromCorner(direction, duration, 0);
    }

    public void swipeFromCorner(MobileElement element, String direction, int duration) {
        swipeInElementFromCorner(element, direction, duration);
    }

    public void doubleTap(MobileElement element) {

        Log.info("Double Tap: "); // + Elements.getElementDetails(element));
        if (Settings.platform == PlatformType.Andorid) {

            Double x = (double) element.getLocation().x
                    + (double) (element.getSize().width / 2);
            Double y = (double) element.getLocation().y
                    + (double) (element.getSize().height / 2);
            JavascriptExecutor js = (JavascriptExecutor) this.client.driver;
            HashMap<String, Double> tapObject = new HashMap<String, Double>();
            tapObject.put("x", x);
            tapObject.put("y", y);
            tapObject.put("touchCount", (double) 1);
            tapObject.put("tapCount", (double) 1);
            tapObject.put("duration", 0.05);
            js.executeScript("mobile: tap", tapObject);
            js.executeScript("mobile: tap", tapObject);
        }
        if (Settings.platform == PlatformType.iOS) {
            RemoteWebElement e = (RemoteWebElement) element;
            ((RemoteWebDriver) this.client.driver).executeScript("au.getElement('" + e.getId() + "').tapWithOptions({tapCount:2});");
        }
    }

    public void longPress(MobileElement element, int duration) {
        Log.info("LongPress: "); // + Elements.getElementDetails(element));
        TouchAction action = new TouchAction(this.client.driver);
        action.press(element).waitAction(duration).release().perform();
    }

    public void pinch(MobileElement element) {

        Log.info("Pinch: "); // + Elements.getElementDetails(element));

        if (Settings.platform == PlatformType.Andorid) {
            TouchAction action1 = new TouchAction(this.client.driver);
            TouchAction action2 = new TouchAction(this.client.driver);

            int elementWidth = element.getSize().width;
            int elementHeight = element.getSize().height;

            action1.press(element, 10, 10).moveTo(element, 50, 50);

            action2.press(element, elementWidth - 10, elementHeight - 10)
                    .moveTo(element, elementWidth - 50, elementHeight - 50);

            MultiTouchAction multiAction = new MultiTouchAction(this.client.driver);
            multiAction.add(action1);
            multiAction.add(action2);
            multiAction.perform();
        }
        if (Settings.platform == PlatformType.iOS) {
            TouchAction action1 = new TouchAction(this.client.driver);
            TouchAction action2 = new TouchAction(this.client.driver);

            int elementWidth = element.getSize().width;
            int elementHeight = element.getSize().height;

            action1.press(element, 10, 10).moveTo(element, 50, 50).release();

            action2.press(element, elementWidth - 10, elementHeight - 10)
                    .moveTo(element, elementWidth - 50, elementHeight - 50)
                    .release();

            MultiTouchAction multiAction = new MultiTouchAction(this.client.driver);
            multiAction.add(action1);
            multiAction.add(action2);
            multiAction.perform();
        }
    }

    public void rotate(MobileElement element) {

        Log.info("Rotate: "); // + Elements.getElementDetails(element));

        if (Settings.platform == PlatformType.Andorid) {
            TouchAction action1 = new TouchAction(this.client.driver);
            TouchAction action2 = new TouchAction(this.client.driver);

            int elementWidth = element.getSize().width;
            int elementHeight = element.getSize().height;

            action1.press(element, 10, 10).moveTo(element, 10, 50);

            action2.press(element, elementWidth - 10, elementHeight - 10)
                    .moveTo(element, elementWidth - 10, elementHeight - 50);

            MultiTouchAction multiAction = new MultiTouchAction(this.client.driver);
            multiAction.add(action1);
            multiAction.add(action2);
            multiAction.perform();
        }
        if (Settings.platform == PlatformType.iOS) {
            TouchAction action1 = new TouchAction(this.client.driver);
            TouchAction action2 = new TouchAction(this.client.driver);

            int elementWidth = element.getSize().width;
            int elementHeight = element.getSize().height;

            action1.press(element, 10, 10).moveTo(element, 10, 50).release();

            action2.press(element, elementWidth - 10, elementHeight - 10)
                    .moveTo(element, elementWidth - 10, elementHeight - 50)
                    .release();

            MultiTouchAction multiAction = new MultiTouchAction(this.client.driver);
            multiAction.add(action1);
            multiAction.add(action2);
            multiAction.perform();
        }
    }

    public void pan(MobileElement element, String direction, int duration) {
        Log.info("Pan: "); // + Elements.getElementDetails(element));
        swipeInElement(element, direction, duration);
    }

    private void swipeInElement(MobileElement element, String direction,
                                int duration) {

        int centerX = element.getLocation().x + (element.getSize().width / 2);
        int centerY = element.getLocation().y + (element.getSize().height / 2);

        int initialX = centerX;
        int initialY = centerY;
        int finalX = centerX;
        int finalY = centerY;

        int offsetXMin = element.getLocation().x
                + (element.getSize().width / 2) - (element.getSize().width / 4);
        int offsetXMax = element.getLocation().x
                + (element.getSize().width / 2) + (element.getSize().width / 4);
        int offsetYMin = element.getLocation().y
                + (element.getSize().height / 2)
                - (element.getSize().height / 4);
        int offsetYMax = element.getLocation().y
                + (element.getSize().height / 2)
                + (element.getSize().height / 4);

        if (direction.equals("down")) {
            initialX = centerX;
            initialY = offsetYMax;
            finalX = centerX;
            finalY = offsetYMin;
        }
        if (direction.equals("up")) {
            initialX = centerX;
            initialY = offsetYMin;
            finalX = centerX;
            finalY = offsetYMax;
        }
        if (direction.equals("left")) {
            initialX = offsetXMax;
            initialY = centerY;
            finalX = offsetXMin;
            finalY = centerY;
        }
        if (direction.equals("right")) {
            initialX = offsetXMin;
            initialY = centerY;
            finalX = offsetXMax;
            finalY = centerY;
        }

        try {
            this.client.driver.swipe(initialX, initialY, finalX, finalY, duration);
            Log.info("Swipe " + direction + " with " + duration
                    + " duration in element with center point "
                    + String.valueOf(centerX) + ":" + String.valueOf(centerY));
        } catch (Exception e) {
            Log.error("Swipe " + direction + " with " + duration
                    + " duration in element with center point "
                    + String.valueOf(centerX) + ":" + String.valueOf(centerY)
                    + "failed.");
        }
    }

    private void swipeInElementFromCorner(MobileElement element, String direction, int duration) {

        int initialX = 0, initialY = 0, finalX = 0, finalY = 0;

        int left = element.getLocation().x;
        int top = element.getLocation().y;
        int right = element.getLocation().x + (element.getSize().width);
        int bottom = element.getLocation().y + (element.getSize().height);

        int offsetLeft = left + (element.getSize().width);
        int offsetTop = top + (element.getSize().height);
        int offsetRight = right - (element.getSize().width);
        int offsetBottom = bottom - (element.getSize().height);

        if (direction.equals("down")) {
            initialX = left;
            initialY = top;
            finalX = left;
            finalY = offsetTop;
        }
        if (direction.equals("up")) {
            initialX = right;
            initialY = bottom;
            finalX = right;
            finalY = offsetBottom;
        }
        if (direction.equals("left")) {
            initialX = right;
            initialY = bottom;
            finalX = offsetRight;
            finalY = bottom;
        }
        if (direction.equals("right")) {
            initialX = left;
            initialY = top;
            finalX = offsetLeft;
            finalY = top;
        }

        try {
            this.client.driver.swipe(initialX, initialY, finalX, finalY, duration);
            Log.info("Swipe " + direction + " with " + duration);
        } catch (Exception e) {
            Log.error("Swipe " + direction + " with " + duration + " failed.");
        }
    }

    /**
     * Scroll down until element is visible via swipe gesture *
     */
    public MobileElement swipeToElement(SwipeElementDirection direction, String elementText, int duration, int retryCount) {
        for (int i = 0; i < retryCount; i++) {
            MobileElement element = this.find.findElementByLocator(Locators.findByTextLocator(elementText, true), 2);
            if ((element != null) && (element.isDisplayed())) {
                Log.info(elementText + " found.");
                return element;
            } else {
                Log.info("Swipe " + direction.toString() + " to " + elementText);
                swipe(direction, duration, Settings.defaultTapDuration * 2);
            }
            if (i == retryCount - 1) {
                Log.error(elementText + " not found after " + String.valueOf(i + 1) + " swipes.");
            }
        }
        return null;
    }

    /**
     * Scroll until element is visible via swipe gesture *
     */
    public MobileElement swipeToElement(SwipeElementDirection direction, By locator, int duration, int retryCount) {
        Log.info("Swipe " + direction.toString() + " to " + locator.toString());
        for (int i = 0; i < retryCount; i++) {
            MobileElement element = this.find.findElementByLocator(locator, 2);
            if ((element != null) && (element.isDisplayed())) {
                Log.info("Element found: " + locator.toString());
                return element;
            } else {
                Log.info("Swipe " + direction.toString() + " to " + locator.toString());
                swipe(direction, duration, Settings.defaultTapDuration * 2);
            }
            if (i == retryCount - 1) {
                Log.info("Element not found after " + String.valueOf(i + 1) + " swipes." + locator.toString());
            }
        }
        return null;
    }
}